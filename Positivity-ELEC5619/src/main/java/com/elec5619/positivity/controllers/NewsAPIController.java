package com.elec5619.positivity.controllers;

import com.elec5619.positivity.domains.Story;
import com.elec5619.positivity.domains.User;
import com.elec5619.positivity.forms.SearchForm;
import com.elec5619.positivity.repositories.OutletPreferenceRepository;
import com.elec5619.positivity.repositories.RegionPreferenceRepository;
import com.elec5619.positivity.repositories.TopicPreferenceRepository;
import com.elec5619.positivity.repositories.UserRepository;
import com.elec5619.positivity.utils.StoryDeserializer;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@PropertySource("classpath:application.properties")
@RequestMapping(path = "/api")
public class NewsAPIController {

    private SimpleDateFormat date_formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicPreferenceRepository topicPreferenceRepository;

    @Autowired
    private OutletPreferenceRepository outletPreferenceRepository;

    @Autowired
    private RegionPreferenceRepository regionPreferenceRepository;

    @Value("${news.api.key}")
    private String newsAPIKey;

    public static String buildQueryStr(List<String> items) {
        String queryStr = "";
        for (int j = 0; j < items.size(); j++) {
            queryStr = queryStr.concat(items.get(j));
            queryStr = queryStr.concat(",");
        }
        queryStr = queryStr.substring(0, queryStr.length() - 1);
        return queryStr;
    }

    public Request buildRequest(List<String> keywords, String topic, List<String> outlets, List<String> regions, String language) {
        HttpUrl.Builder base_builder = new HttpUrl.Builder()
                .scheme("https")
                .host("api.newscatcherapi.com")
                .addPathSegment("v2")
                .addPathSegment("search")
                .addQueryParameter("lang", language)
                .addQueryParameter("topic", topic);

        if (keywords.size() != 0) {
            StringBuilder queryParameter = new StringBuilder("");
            for (int i = 0; i < keywords.size(); i++) {
                queryParameter.append(keywords.get(i));
                if (i != keywords.size() - 1) {
                    queryParameter.append(" || ");
                }
            }
            base_builder.addQueryParameter("q", queryParameter.toString());
        } else {
            base_builder.addQueryParameter("q", "news");
        }

        if (outlets.size() != 0) {
            base_builder.addQueryParameter("sources", buildQueryStr(outlets));
        }

        if (regions.size() != 0) {
            base_builder.addQueryParameter("countries", buildQueryStr(regions));
        }

        HttpUrl url = base_builder.build();

        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .header("x-api-key", newsAPIKey)
                .build();

        return request;
    }

    public List<Story> handleStories(String responseStr) {

        List<Story> stories = new ArrayList<>();

        JsonObject jsonObject = JsonParser.parseString(responseStr).getAsJsonObject();
        if (jsonObject.get("articles") == null) {
            return new ArrayList<>();
        }
        JsonArray storiesJSON = jsonObject.get("articles").getAsJsonArray();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Story.class, new StoryDeserializer());

        for (int j = 0; j < storiesJSON.size(); j++) {
            try {
                stories.add(gsonBuilder.create().fromJson(storiesJSON.get(j).getAsJsonObject(), Story.class));

            } catch (IllegalArgumentException e) {
            }
        }

        return stories;

    }

    @GetMapping(value = "/my-stories")
    @ResponseBody
    public List<Story> getPreferenceStories(HttpSession session) throws IOException {

        Integer currentUserId = (Integer) session.getAttribute("user");

        List<String> topics = topicPreferenceRepository.getTopicPreferenceNamesByUserId(currentUserId);
        List<String> outlets = outletPreferenceRepository.getOutletPreferenceNamesByUserId(currentUserId);
        List<String> regions = regionPreferenceRepository.getRegionPreferencesByUserId(currentUserId);

        Map<String, String> countries = new HashMap<>();
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry(), iso);
        }

        ArrayList<String> countryList = new ArrayList<>();
        for (String country : regions) {
            countryList.add(countries.get(country));
        }

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = buildRequest(topics, "news", outlets, countryList, "en");
        Response response = client.newCall(request).execute();

        if (response.code() == 429) {
            String error = "Invalid API key (or reached monthly limit) for External API NewsCatcher. Try again later.";
            System.out.println(error);
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, error);
        }

        if (response.code() == 401) {
            String error = "Invalid API key (or reached monthly limit) for External API NewsCatcher. Try again later.";
            System.out.println(error);
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, error);
        }

        List<Story> stories = handleStories(response.body().string());
        Collections.shuffle(stories);
        return stories;

    }

    @PostMapping(value = "/search")
    @ResponseBody
    public List<Story> getSearchStories(@ModelAttribute SearchForm searchForm, HttpSession session) throws IOException {

        List<String> keyWordsList = new ArrayList<>();
        if (searchForm.getKeywords().replaceAll("\\s+", "") != "" && searchForm.getKeywords() != null) {
            String[] keywordsStrList = searchForm.getKeywords().split(",");
            keyWordsList = Arrays.asList(keywordsStrList);
        }

        List<String> outlets = new ArrayList<>();
        if (searchForm.getOutlet() != "" && searchForm.getOutlet() != null) {
            outlets = Arrays.asList(searchForm.getOutlet());
        }

        List<String> regions = new ArrayList<>();
        if (searchForm.getRegion() != "" && searchForm.getRegion() != null) {
            regions = Arrays.asList(searchForm.getRegion());
        }

        Map<String, String> countries = new HashMap<>();
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry(), iso);
        }

        ArrayList<String> countryList = new ArrayList<>();
        for (String country : regions) {
            countryList.add(countries.get(country));
        }

        if (searchForm.getLanguage() == "" || searchForm.getLanguage() == null) {
            searchForm.setLanguage("en");
        }

        boolean languageFound = false;
        for (Locale locale : Locale.getAvailableLocales()) {
            if (searchForm.getLanguage().equals(locale.getDisplayLanguage())) {
                searchForm.setLanguage(locale.getLanguage());
                languageFound = true;
            }
        }
        if (!languageFound) {
            searchForm.setLanguage("en");
        }

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = buildRequest(keyWordsList, searchForm.getTopic(), outlets, countryList, searchForm.getLanguage());
        Response response = client.newCall(request).execute();

        if (response.code() == 429) {
            String error = "Invalid API key (or reached monthly limit) for External API NewsCatcher. Try again later.";
            System.out.println(error);
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, error);
        }

        if (response.code() == 401) {
            String error = "Invalid API key (or reached monthly limit) for External API NewsCatcher. Try again later.";
            System.out.println(error);
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, error);
        }

        List<Story> stories = handleStories(response.body().string());
        if (searchForm.getOrderBy().equals("date")) {
            Collections.sort(stories, new Comparator<Story>() {
                public int compare(Story s1, Story s2) {
                    return s2.getPublishedDate().compareTo(s1.getPublishedDate());
                }
            });
        }

        return stories;

    }
}
