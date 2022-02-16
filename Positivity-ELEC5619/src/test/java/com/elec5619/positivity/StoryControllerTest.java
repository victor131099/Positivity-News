package com.elec5619.positivity;

import com.elec5619.positivity.domains.Story;
import com.elec5619.positivity.domains.User;
import com.elec5619.positivity.repositories.StoryRepository;
import com.elec5619.positivity.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")

public class StoryControllerTest {

    @Autowired
    StoryRepository storyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void likeStory() throws Exception {
        User user = new User(
                "storyController_user0_first",
                "storyController_user0_last",
                "storyController_user0@storyController.com",
                "storyController_password123"
        );
        userRepository.save(user);

        Story s = new Story(
                "storyController_story0",
                "b",
                "c",
                "d",
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
                .get("/likeStory?storyID=" + s.getId())
                .sessionAttr("user", user.getId()));
        Optional<Story> story = storyRepository.findById(s.getId());

        assertTrue(story.isPresent());
        assertTrue(story.get().getLikes() == 1);
    }

    @Test
    public void unlikeStory() throws Exception {
        User user = new User(
                "storyController_user1_first",
                "storyController_user1_last",
                "storyController_user1@storyController.com",
                "storyController_password123"
        );
        userRepository.save(user);

        Story s = new Story(
                "storyController_story1",
                "b",
                "c",
                "d",
                "e",
                Calendar.getInstance().getTime(),
                "g",
                "h",
                "i",
                "j",
                "k",
                1,
                0
        );
        storyRepository.save(s);

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/unlikeStory?storyID=" + s.getId())
                .sessionAttr("user", user.getId()));
        Optional<Story> story = storyRepository.findById(s.getId());
        assertTrue(story.isPresent());
        assertTrue(story.get().getLikes() == 0);
    }
}

