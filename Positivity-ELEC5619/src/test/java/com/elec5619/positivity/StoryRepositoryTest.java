package com.elec5619.positivity;


import com.elec5619.positivity.domains.Story;
import com.elec5619.positivity.repositories.StoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")

public class StoryRepositoryTest {

    @Autowired
    StoryRepository storyRepository;

    @Test
    public void findByIdEmpty() {
        Optional<Story> story = storyRepository.findById("a");
        assertFalse(story.isPresent());
    }

    @Test
    public void findByIdPresent() {
        Story s = new Story(
                "storyRepository_story0",
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
        Optional<Story> story = storyRepository.findById(s.getId());
        assertTrue(story.isPresent());
    }

    @Test
    public void getAllIds() {
        Story s1 = new Story(
                "storyRepository_story1",
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
        Story s2 = new Story(
                "storyRepository_story2",
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
        Story s3 = new Story(
                "storyRepository_story3",
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
        storyRepository.save(s1);
        storyRepository.save(s2);
        storyRepository.save(s3);

        List<String> storyIds = storyRepository.getAllIds();

        assertTrue(storyIds.contains(s1.getId()));
        assertTrue(storyIds.contains(s2.getId()));
        assertTrue(storyIds.contains(s3.getId()));
    }
}

