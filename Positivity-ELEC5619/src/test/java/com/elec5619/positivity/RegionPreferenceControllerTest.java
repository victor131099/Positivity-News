package com.elec5619.positivity;

import com.elec5619.positivity.domains.User;
import com.elec5619.positivity.repositories.RegionPreferenceRepository;
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

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")

public class RegionPreferenceControllerTest {
    @Autowired
    RegionPreferenceRepository regionPreferenceRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    }

    @Test
    public void selectRegionPreference() throws Exception {
        User user = new User(
                "regionPreference_user0_first",
                "regionPreference_user0_last",
                "regionPreference_user0@regionPreference.com",
                "regionPreference123"
        );
        this.userRepository.save(user);

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/countries")
                .param("country", "Australia")
                .sessionAttr("user", user.getId())
        ).andExpect(status().isOk());
        List<String> rp = regionPreferenceRepository.getRegionPreferencesByUserId(user.getId());
        assertEquals(rp.size(), 1);
        assertEquals(rp.get(0), "Australia");

    }

    @Test
    public void selectMultipleRegionPreference() throws Exception {
        User user = new User(
                "regionPreference_user1_first",
                "regionPreference_user1_last",
                "regionPreference_user1@regionPreference.com",
                "regionPreference123"
        );
        this.userRepository.save(user);

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/countries")
                .param("country", "Australia")
                .sessionAttr("user", user.getId())
        ).andExpect(status().isOk());

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/countries")
                .param("country", "Canada")
                .sessionAttr("user", user.getId())
        ).andExpect(status().isOk());

        List<String> rp = regionPreferenceRepository.getRegionPreferencesByUserId(user.getId());
        Collections.sort(rp);
        assertEquals(rp.size(), 2);
        assertEquals(rp.get(0), "Australia");
        assertEquals(rp.get(1), "Canada");
    }

    @Test
    public void deselectMultipleRegionPreference() throws Exception {
        User user = new User(
                "regionPreference_user2_first",
                "regionPreference_user2_last",
                "regionPreference_user2@regionPreference.com",
                "regionPreference123"
        );
        this.userRepository.save(user);

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/countries")
                .param("country", "Australia")
                .sessionAttr("user", user.getId())
        ).andExpect(status().isOk());

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/countries")
                .param("country", "Canada")
                .sessionAttr("user", user.getId())
        ).andExpect(status().isOk());

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/countries")
                .param("country", "Canada")
                .sessionAttr("user", user.getId())
        ).andExpect(status().isOk());

        List<String> rp = regionPreferenceRepository.getRegionPreferencesByUserId(user.getId());
        assertEquals(rp.size(), 1);
        assertEquals(rp.get(0), "Australia");
    }
}
