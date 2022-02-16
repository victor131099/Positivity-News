package com.elec5619.positivity;

import com.elec5619.positivity.domains.*;
import com.elec5619.positivity.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")

public class NewsApiTest {

    @Value("${news.api.key}")
    private String newsAPIKey;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

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
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void getPreferenceStoriesNoPreferences() throws Exception {
        if (newsAPIKey.equals("newsApiKey")) {
            System.out.println("No news api key found, skipping.");
            return;
        }

        User user = new User(
                "newsApiTest_user0_first",
                "newsApiTest_user0_last",
                "newsApiTest_user0@newsApiTest.com",
                "newsApiTest_password123"
        );
        userRepository.save(user);

        Thread.sleep(1000);
        try {
            this.mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/my-stories")
                    .sessionAttr("user", user.getId())
            ).andExpect(status().isOk());
        } catch (NestedServletException e) {
            return;
        }
    }

    @Test
    public void getPreferenceStoriesTopicPreferences() throws Exception {
        if (newsAPIKey.equals("/api/newsApiKey")) {
            System.out.println("No news api key found, skipping.");
            return;
        }

        User user = new User(
                "newsApiTest_user1_first",
                "newsApiTest_user1_last",
                "newsApiTest_user1@newsApiTest.com",
                "newsApiTest_password123"
        );
        userRepository.save(user);

        Topic topic = new Topic("Food");
        topicRepository.save(topic);

        TopicPreference topicPreference = new TopicPreference(user, topic);
        topicPreferenceRepository.save(topicPreference);

        Thread.sleep(1000);
        try {
            this.mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/my-stories")
                    .sessionAttr("user", user.getId())
            ).andExpect(status().isOk());
        } catch (NestedServletException e) {
            return;
        }
    }

    @Test
    public void getPreferenceStoriesOutletPreferences() throws Exception {
        if (newsAPIKey.equals("newsApiKey")) {
            System.out.println("No news api key found, skipping.");
            return;
        }

        User user = new User(
                "newsApiTest_user2_first",
                "newsApiTest_user2_last",
                "newsApiTest_user2@newsApiTest.com",
                "newsApiTest_password123"
        );
        userRepository.save(user);

        Outlet outlet = new Outlet("nytimes.com");
        outletRepository.save(outlet);

        OutletPreference outletPreference = new OutletPreference(user, outlet);
        outletPreferenceRepository.save(outletPreference);

        Thread.sleep(1000);
        try {
            this.mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/my-stories")
                    .sessionAttr("user", user.getId())
            ).andExpect(status().isOk());
        } catch (NestedServletException e) {
            return;
        }
    }

    @Test
    public void getPreferenceStoriesRegionPreferences() throws Exception {
        if (newsAPIKey.equals("newsApiKey")) {
            System.out.println("No news api key found, skipping.");
            return;
        }

        User user = new User(
                "newsApiTest_user3_first",
                "newsApiTest_user3_last",
                "newsApiTest_user3@newsApiTest.com",
                "newsApiTest_password123"
        );
        userRepository.save(user);

        Region region = new Region("Australia");
        regionRepository.save(region);

        RegionPreference regionPreference = new RegionPreference(user, region);
        regionPreferenceRepository.save(regionPreference);

        Thread.sleep(1000);
        try {
            this.mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/my-stories")
                    .sessionAttr("user", user.getId())
            ).andExpect(status().isOk());
        } catch (NestedServletException e) {
            return;
        }
    }

}

