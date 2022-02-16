package com.elec5619.positivity;

import com.elec5619.positivity.domains.Story;
import com.elec5619.positivity.domains.User;
import com.elec5619.positivity.repositories.StoryRepository;
import com.elec5619.positivity.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Calendar;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")

public class WebControllerTest {

    @Autowired
    StoryRepository storyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void viewStoryNotPresent() throws Exception {
        User user = new User(
                "webController_user0_first",
                "webController_user0_last",
                "webController_user0@webController.com",
                "webController_password123"
        );
        userRepository.save(user);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/story?storyID=doesnotexist")
                        .sessionAttr("user", user.getId()))
                .andExpect(content().string(containsString("Whoops!"))).andExpect(status().isOk());
    }

    @Test
    public void viewStory() throws Exception {
        User user = new User(
                "webController_user1_first",
                "webController_user1_last",
                "webController_user1@webController.com",
                "webController_password123"
        );
        userRepository.save(user);
        Story s = new Story(
                "webController_story0",
                "b",
                "c",
                "test_title",
                "e",
                Calendar.getInstance().getTime(),
                "g",
                "h",
                "i",
                "j",
                "k",
                0,
                0
        );
        storyRepository.save(s);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/story?storyID=" + s.getId())
                        .sessionAttr("user", user.getId()))
                .andExpect(content().string(containsString("test_title"))).andExpect(status().isOk());
        Optional<Story> story = storyRepository.findById(s.getId());
        assertTrue(story.isPresent());
        assertTrue(story.get().getViews() == 1);
    }

    @Test
    public void createStoryNotPresent() throws Exception {
        User user = new User(
                "webController_user2_first",
                "webController_user2_last",
                "webController_user2@webController.com",
                "webController_password123"
        );
        userRepository.save(user);
        Story story = new Story(
                "webController_story2",
                "b",
                "c",
                "test_title",
                "e",
                Calendar.getInstance().getTime(),
                "g",
                "h",
                "i",
                "j",
                "k",
                0,
                0
        );
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/story")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(story))
                        .sessionAttr("user", user.getId()))
                .andExpect(content().string(containsString("test_title"))).andExpect(status().isOk());
    }

    @Test
    public void createStoryPresent() throws Exception {
        User user = new User(
                "webController_user3_first",
                "webController_user3_last",
                "webController_user3@webController.com",
                "webController_password123"
        );
        userRepository.save(user);
        Story story = new Story(
                "webController_story3",
                "b",
                "c",
                "test_title",
                "e",
                Calendar.getInstance().getTime(),
                "g",
                "h",
                "i",
                "j",
                "k",
                0,
                0
        );
        storyRepository.save(story);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/story")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(story))
                        .sessionAttr("user", user.getId()))
                .andExpect(redirectedUrl("/story?storyID=" + story.getId())).andExpect(status().is3xxRedirection());
    }

    @Test
    public void viewHistory() throws Exception {
        User user = new User(
                "webController_user4_first",
                "webController_user4_last",
                "webController_user4@webController.com",
                "webController_password123"
        );
        userRepository.save(user);
        Story story = new Story(
                "webController_story5",
                "b",
                "c",
                "test_title",
                "e",
                Calendar.getInstance().getTime(),
                "g",
                "h",
                "i",
                "j",
                "k",
                0,
                0
        );
        storyRepository.save(story);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/story?storyID=" + story.getId())
                        .sessionAttr("user", user.getId()))
                .andExpect(content().string(containsString("test_title"))).andExpect(status().isOk());
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/history")
                        .sessionAttr("user", user.getId()))
                .andExpect(content().string(containsString("test_title"))).andExpect(status().isOk());
    }
}

