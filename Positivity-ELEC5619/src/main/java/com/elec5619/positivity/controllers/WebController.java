package com.elec5619.positivity.controllers;

import com.elec5619.positivity.domains.*;
import com.elec5619.positivity.forms.SignInForm;
import com.elec5619.positivity.forms.SearchForm;
import com.elec5619.positivity.forms.SettingsForm;
import com.elec5619.positivity.forms.SignUpForm;
import com.elec5619.positivity.repositories.*;
import com.elec5619.positivity.utils.Encryption;
import edu.stanford.nlp.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Controller
public class WebController implements WebMvcConfigurer {
    @Autowired
    private UserRepository userRepository;
    public static final Properties defaultProperties = new Properties();

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private TopicPreferenceRepository topicPreferenceRepository;

    @Autowired
    private OutletRepository outletRepository;

    @Autowired
    private OutletPreferenceRepository outletPreferenceRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RegionPreferenceRepository regionPreferenceRepository;

    @Autowired
    private LikedRepository likedRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Value("${sentiment.api.key}")
    private String sentimentAPIKey;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("homepage");
    }

    @GetMapping("/signup")
    public String showRegisterForm(SignUpForm signUpForm) {
        return "signup";
    }

    @PostMapping("/signup")
    public String checkSignUpForm(SignUpForm signUpForm, Model model, HttpSession session) {
        List<User> users = userRepository.findByEmail(signUpForm.getEmail());

        if (users.size() != 0) {
            model.addAttribute("emailError", true);
            return "signup";
        }

        User n = new User(signUpForm.getFirstName(), signUpForm.getLastName(), signUpForm.getEmail(), signUpForm.getPassword());
        userRepository.save(n);
        session.setAttribute("user", n.getId());

        return "redirect:/home";
    }

    @GetMapping("/signin")
    public String showSignInForm(SignInForm signInForm) throws IOException, ParseException {
        return "signin";
    }

    @PostMapping("/signin")
    public String checkSignInForm(SignInForm signInForm, Model model, HttpSession session) throws IOException, ParseException {
        List<User> users = userRepository.findByEmail(signInForm.getEmail());

        if (users.size() == 0) {
            model.addAttribute("emailError", true);
            return "signin";
        }

        for (User user : users) {
            if (user.getPassword().equals(Encryption.encryptSHA256(signInForm.getPassword()))) {
                session.setAttribute("user", user.getId());
                return "redirect:/home";
            }
        }

        model.addAttribute("passwordError", true);
        return "signin";
    }

    @PostMapping("/logout")
    public String logout(Model model, SignInForm signInForm, HttpSession session)  {
        session.invalidate();
        return "redirect:/signin";
    }

    @GetMapping("/home")
    public String getHome(Model model, HttpSession session, SearchForm searchForm) {

        Integer currentUserId = (Integer) session.getAttribute("user");
        if (!(currentUserId instanceof Integer)) {
            return "error";
        }

        List<String> topics = topicRepository.getAllTopicNames();
        model.addAttribute("topics", topics);

        List<String> outlets = outletRepository.getAllOutletNames();
        model.addAttribute("outlets", outlets);

        List<String> regions = regionRepository.getAllRegionNames();
        model.addAttribute("regions", regions);

        List<String> language_codes = languageRepository.getAllLanguageNames();
        List<String> languages = new ArrayList<>();

        for (String language: language_codes) {
            Locale locale = new Locale(language);
            languages.add(locale.getDisplayLanguage());
        }

        model.addAttribute("languages", languages);

        model.addAttribute("sentimentAPIKey", sentimentAPIKey);


        return "homepage";
    }

    public Pair<Integer[], User> checkSettingsFieldChanged(SettingsForm settingsForm, User user) {
        Integer[] fieldsChanged = new Integer[4];
        Arrays.fill(fieldsChanged, 0);
        if (settingsForm.getFirstName().trim().compareTo("") != 0 && settingsForm.getFirstName().trim().compareTo(user.getFirstName().trim()) != 0) {
            user.setFirstName(settingsForm.getFirstName());
            fieldsChanged[0] = 1;
        }
        if (settingsForm.getLastName().trim().compareTo("") != 0 && settingsForm.getLastName().trim().compareTo(user.getLastName().trim()) != 0) {
            user.setLastName(settingsForm.getLastName());
            fieldsChanged[1] = 1;
        }
        if (settingsForm.getEmail().trim().compareTo("") != 0 && settingsForm.getEmail().trim().compareTo(user.getEmail().trim()) != 0) {
            user.setEmail(settingsForm.getEmail());
            fieldsChanged[2] = 1;
        }
        if (settingsForm.getPassword().trim().compareTo("") != 0 && Encryption.encryptSHA256(settingsForm.getPassword()).compareTo(user.getPassword()) != 0) {
            user.setPassword((settingsForm.getPassword()));
            fieldsChanged[3] = 1;
        }
        return new Pair<Integer[], User>(fieldsChanged, user);
    }

    @PostMapping("/settings")
    public String checkSettingsForm(SettingsForm settingsForm, Model model, HttpSession session) {
        Object rawCurrentUserId = session.getAttribute("user");
        if(!(rawCurrentUserId instanceof Integer currentUserId)) {
            model.addAttribute("currentSessionError", true);
            return "settings";
        }

        User user = userRepository.findById(currentUserId).get();
        String currentPassword = user.getPassword();
        if (!Encryption.encryptSHA256(settingsForm.getOldPassword()).equals(currentPassword)) {
            model.addAttribute("currentPasswordError", true);
            return "settings";
        }
        List<User> existingEmail = userRepository.findByEmail(settingsForm.getEmail());
        if (existingEmail.size() != 0 && !existingEmail.get(0).getId().equals(currentUserId)) {
            model.addAttribute("emailError", true);
            return "settings";
        }
        Pair<Integer[], User> fieldsChangedNewUserTuple = checkSettingsFieldChanged(settingsForm, user);
        Integer[] fieldsChanged = fieldsChangedNewUserTuple.first();
        User potentialNewUser = fieldsChangedNewUserTuple.second();
        Integer sum = 0;
        for (Integer value : fieldsChanged) {
            sum += value;
        }
        if (sum.equals(0)) { //nothing was actually changed
            model.addAttribute("noMeaningfulChangeError", true);
            return "settings";
        }
        if (fieldsChanged[2].equals(1) || fieldsChanged[3].equals(1)) { //if email and password changed we need to reset the session and redirect to signin
            userRepository.save(potentialNewUser);
            session.invalidate();
            return "redirect:/signin";
        } else { //changing first name and last name does not require session reset. Redirect to home page instead
            userRepository.save(potentialNewUser);
            return "redirect:/home";
        }
    }

    @GetMapping("/settings")
    public String getSettings(SettingsForm settingsForm, Model model, HttpSession session) {

        Integer currentUserId = (Integer) session.getAttribute("user");
        if (!(currentUserId instanceof Integer)) {
            return "error";
        }

        return "settings";
    }

    @GetMapping("/preferences")
    public String getPreferences(Model model, HttpSession session) { //, HttpSession session) {

        Integer currentUserId = (Integer) session.getAttribute("user");
        if (!(currentUserId instanceof Integer)) {
            return "error";
        }

        // Topics
        List<Topic> userTopics = topicPreferenceRepository.getTopicPreferencesByUserId(currentUserId);
        List<Topic> allTopics = (List<Topic>) topicRepository.findAll();

        List<Topic> nonUserTopics = new ArrayList<>(allTopics);
        nonUserTopics.removeAll(userTopics);

        model.addAttribute("myTopics", userTopics);
        model.addAttribute("allTopics", nonUserTopics);

        //Regions
        List<String> regions= (List<String>) regionPreferenceRepository.getRegionPreferencesByUserId(currentUserId);
        model.addAttribute("regions", regions);

        // Outlets
        List<Outlet> userOutlets = outletPreferenceRepository.getOutletPreferencesByUserId(currentUserId);
        List<Outlet> allOutlets = (List<Outlet>) outletRepository.findAll();

        List<Outlet> nonUserOutlets = new ArrayList<>(allOutlets);
        nonUserOutlets.removeAll(userOutlets);

        model.addAttribute("myOutlets", userOutlets);
        model.addAttribute("allOutlets", nonUserOutlets);

        return "preferences";
    }

    @GetMapping("/story")
    public String getStory(@RequestParam(value = "storyID") String storyID, Model model, HttpSession session) {

        Integer currentUserId = (Integer) session.getAttribute("user");
        if (!(currentUserId instanceof Integer)) {
            return "error";
        }

        Optional<Story> story = storyRepository.findById(storyID);

        if(!story.isPresent()) {
            return "error";
        }

        Optional<User> user = userRepository.findById(currentUserId);

        story.get().setViews(story.get().getViews() + 1);
        storyRepository.save(story.get());
        Optional<History> history = historyRepository.findByUserAndStory(user.get(), story.get());

        if (history.isPresent()) {
            History history1 = history.get();
            history1.setTime(Calendar.getInstance().getTime());
            historyRepository.save(history1);
        }

        else {
            historyRepository.save(new History(user.get(), story.get(), Calendar.getInstance().getTime()));
        }

        model.addAttribute("story", story.get());

        Optional<Liked> liked = likedRepository.findByUserAndStory(user.get(), story.get());

        if (liked.isPresent()) {
            model.addAttribute("liked", true);
        }

        return "story";
    }

    @RequestMapping(method=RequestMethod.POST, value="/story")
    public String getStory(@RequestBody Story newStory, Model model, HttpSession session) {

        Integer currentUserId = (Integer) session.getAttribute("user");
        if(!(currentUserId instanceof Integer)) {
            return "error";
        }

        Optional<Story> story = storyRepository.findById(newStory.getId());

        if(story.isPresent()) {
            return "redirect:/story?storyID=" + newStory.getId();
        }

        Optional<User> user = userRepository.findById(currentUserId);

        newStory.setViews(1);
        storyRepository.save(newStory);
        Optional<History> history = historyRepository.findByUserAndStory(user.get(), newStory);

        if (history.isPresent()) {
            History history1 = history.get();
            history1.setTime(Calendar.getInstance().getTime());
            historyRepository.save(history1);
        }

        else {
            historyRepository.save(new History(user.get(), newStory, Calendar.getInstance().getTime()));
        }

        model.addAttribute("story", newStory);

        Optional<Liked> liked = likedRepository.findByUserAndStory(user.get(), newStory);

        if (liked.isPresent()) {
            model.addAttribute("liked", true);
        }

        return "story";
    }

    @GetMapping("/history")
    public String getHistory(Model model, HttpSession session) {

        Integer currentUserId = (Integer) session.getAttribute("user");
        if(!(currentUserId instanceof Integer)) {
            return "error";
        }

        List<History> histories = historyRepository.findByUserOrderByTimeDesc(userRepository.findById(currentUserId).get());
        List<Story> stories = new ArrayList<>();

        for (int i = 0; i < histories.size(); i++) {
            stories.add(histories.get(i).getStory());
            if (i == 9) {
                break;
            }
        }

        model.addAttribute("stories", stories);

        return "history";
    }
}
