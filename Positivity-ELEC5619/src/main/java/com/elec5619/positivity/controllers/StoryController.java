package com.elec5619.positivity.controllers;

import com.elec5619.positivity.domains.Liked;
import com.elec5619.positivity.domains.Story;
import com.elec5619.positivity.domains.User;
import com.elec5619.positivity.repositories.HistoryRepository;
import com.elec5619.positivity.repositories.LikedRepository;
import com.elec5619.positivity.repositories.StoryRepository;
import com.elec5619.positivity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
public class StoryController {
    @Autowired
    private LikedRepository likedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @GetMapping("/likeStory")
    public Integer likeStory(@RequestParam(value = "storyID") String storyID, HttpSession session) {
        Integer currentUserId = (Integer) session.getAttribute("user");
        Optional<User> user = userRepository.findById(currentUserId);
        Optional<Story> story = storyRepository.findById(storyID);
        Story story1 = story.get();
        story1.setLikes(story1.getLikes() + 1);
        storyRepository.save(story1);
        likedRepository.save(new Liked(user.get(), story1));
        return story1.getLikes();
    }

    @GetMapping("/unlikeStory")
    public Integer unlikeStory(@RequestParam(value = "storyID") String storyID, HttpSession session) {
        Integer currentUserId = (Integer) session.getAttribute("user");
        Optional<User> user = userRepository.findById(currentUserId);
        Optional<Story> story = storyRepository.findById(storyID);
        Story story1 = story.get();
        story1.setLikes(story1.getLikes() - 1);
        storyRepository.save(story1);
        Optional<Liked> liked = likedRepository.findByUserAndStory(user.get(), story1);
        if (liked.isPresent()) {
            likedRepository.delete(liked.get());
        }

        return story1.getLikes();
    }
}
