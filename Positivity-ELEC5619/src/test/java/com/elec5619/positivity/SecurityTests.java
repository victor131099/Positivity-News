package com.elec5619.positivity;


import com.elec5619.positivity.domains.User;
import com.elec5619.positivity.repositories.UserRepository;
import com.elec5619.positivity.utils.Encryption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")

public class SecurityTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void encryptSHA256Correct() throws Exception {
        String input;
        String expected;
        input = "abc123";
        expected = "6ca13d52ca70c883e0f0bb101e425a89e8624de51db2d2392593af6a84118090";
        assertEquals(Encryption.encryptSHA256(input), expected);
        input = "justalpha";
        expected = "319c1385a7233dce3154fd53a2ed6039e3bb84fa9ea752b829b03f9d095c4cdc";
        assertEquals(Encryption.encryptSHA256(input), expected);
        input = "0123456789";
        expected = "84d89877f0d4041efb6bf91a16f0248f2fd573e6af05c19f96bedb9f882f7882";
        assertEquals(Encryption.encryptSHA256(input), expected);
        input = "";
        expected = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        assertEquals(Encryption.encryptSHA256(input), expected);
    }

    @Test
    public void userModelHashesPassword() throws Exception {
        User user = new User(
                "securityTest_user0_first",
                "securityTest_user0_last",
                "securityTest_user0@securityTest.com",
                "securityTest_password123"
        );
        assertEquals(user.getPassword(), Encryption.encryptSHA256("securityTest_password123"));
    }

    @Test
    public void signupHashesPassword() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("firstName", "securityTest_user1_first")
                .param("lastName", "securityTest_user1_last")
                .param("email", "securityTest_user1@securityTest.com")
                .param("password", "securityTest_password123")
        ).andExpect(redirectedUrl("/home")).andExpect(status().is3xxRedirection());

        List<User> users = userRepository.findByEmail("securityTest_user1@securityTest.com");
        assertEquals(users.size(), 1);
        assertEquals(users.get(0).getPassword(), Encryption.encryptSHA256("securityTest_password123"));
    }

    @Test
    public void signinCreatesSession() throws Exception {
        User user = new User(
                "securityTest_user2_first",
                "securityTest_user2_last",
                "securityTest_user2@securityTest.com",
                "securityTest_password123"
        );
        userRepository.save(user);
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/signin")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("email", "securityTest_user2@securityTest.com")
                .param("password", "securityTest_password123")
        ).andReturn();

        // Fetches session without creating one if it does not exist
        var session = (MockHttpSession) result.getRequest().getSession(false);

        assertNotNull(session);
        assertEquals(session.getAttribute("user"), user.getId());
    }
}

