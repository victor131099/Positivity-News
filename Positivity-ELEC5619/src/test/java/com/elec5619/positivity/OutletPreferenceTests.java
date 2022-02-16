package com.elec5619.positivity;


import com.elec5619.positivity.domains.Outlet;
import com.elec5619.positivity.domains.OutletPreference;
import com.elec5619.positivity.domains.User;
import com.elec5619.positivity.repositories.OutletPreferenceRepository;
import com.elec5619.positivity.repositories.OutletRepository;
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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")

public class OutletPreferenceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OutletRepository outletRepository;

    @Autowired
    private OutletPreferenceRepository outletPreferenceRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void putOutletPreference() throws Exception {
        User user = new User(
                "outletPreferenceTest_user0_first",
                "outletPreferenceTest_user0_last",
                "outletPreferenceTest_user0@outletPreferenceTest.com",
                "outletPreferenceTest_password123"
        );
        userRepository.save(user);

        Outlet outlet = new Outlet("abc.com");
        outletRepository.save(outlet);

        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/api/outletPreference")
                .param("outlet_name", outlet.getOutlet_name())
                .sessionAttr("user", user.getId())
        ).andExpect(status().isOk());

        List<Outlet> outlets = outletPreferenceRepository.getOutletPreferencesByUserId(user.getId());

        assertEquals(outlets.size(), 1);
        assertEquals(outlets.get(0).getOutlet_name(), outlet.getOutlet_name());

    }

    @Test
    public void deleteOutletPreference() throws Exception {
        User user = new User(
                "outletPreferenceTest_user1_first",
                "outletPreferenceTest_user1_last",
                "outletPreferenceTest_user1@outletPreferenceTest.com",
                "outletPreferenceTest_password123"
        );
        userRepository.save(user);

        Outlet outlet = new Outlet("9news.com");
        outletRepository.save(outlet);

        OutletPreference outletPreference = new OutletPreference(user, outlet);
        outletPreferenceRepository.save(outletPreference);

        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/outletPreference")
                .param("outlet_name", outlet.getOutlet_name())
                .sessionAttr("user", user.getId())
        ).andExpect(status().isOk());

        List<Outlet> outlets = outletPreferenceRepository.getOutletPreferencesByUserId(user.getId());

        assertEquals(outlets.size(), 0);
    }

}

