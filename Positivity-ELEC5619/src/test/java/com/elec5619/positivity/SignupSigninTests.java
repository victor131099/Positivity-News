package com.elec5619.positivity;


import com.elec5619.positivity.domains.User;
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

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@RunWith(SpringRunner.class)
//@DataJpaTest
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")

public class SignupSigninTests {

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;


    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    //write tests for signup, login, changing user settings

    @Test
    public void validSignup() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("firstName", "signupSignin_user1_first")
                .param("lastName", "signupSignin_user1_last")
                .param("email", "user1@signupSignin.com")
                .param("password", "signupSignin_password123")
        ).andExpect(redirectedUrl("/home")).andExpect(status().is3xxRedirection());
        List<User> users = userRepository.findByEmail("user1@signupSignin.com");
        assertEquals(users.size(), 1);
        //assertEquals(CONTENT_TYPE, mvcResult.getResponse().getContentType());
    }

    @Test
    public void invalidSignup() throws Exception {
        User u = new User(
                "signupSignin_exists_first",
                "signupSignin_exists_last",
                "signupSignin_exists@signupSignin.com",
                "signupSignin_password123"
        );
        userRepository.save(u);

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("firstName", "signupSignin_user2_first")
                .param("lastName", "signupSignin_user2_last")
                .param("email", "signupSignin_exists@signupSignin.com")
                .param("password", "signupSignin_password123")
        ).andExpect(content().string(containsString(
                "This email is already associated with another account"
        ))).andExpect(status().isOk());

        List<User> users = userRepository.findByEmail("signupSignin_exists@signupSignin.com");
        assertEquals(users.size(), 1);

    }

    @Test
    public void validSignin() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("firstName", "signupSignin_user3_first")
                .param("lastName", "signupSignin_user3_last")
                .param("email", "user3@signupSignin.com")
                .param("password", "signupSignin_password123")
        ).andExpect(redirectedUrl("/home")).andExpect(status().is3xxRedirection());
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/signin")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("email", "user3@signupSignin.com")
                .param("password", "signupSignin_password123")
        ).andExpect(redirectedUrl("/home")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void changeSettingsFieldsInputFirstNameOnly() throws Exception {
        User user = new User(
                "signupSignin_user6_first",
                "signupSignin_user6_last",
                "user6@signupSignin.com",
                "signupSignin_password123"
        );
        userRepository.save(user);
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/settings")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("firstName", "signupSignin_user6_newFirst")
                .param("lastName", "")
                .param("email", "")
                .param("password", "")
                .param("confirmPassword", "")
                .param("oldPassword", "signupSignin_password123")
                .sessionAttr("user", user.getId())
        ).andExpect(redirectedUrl("/home")).andExpect(status().is3xxRedirection());

        List<User> users = userRepository.findByEmail("user6@signupSignin.com");
        assertEquals(users.size(), 1);
        User newUser = new User(
                "signupSignin_user6_newFirst",
                "signupSignin_user6_last",
                "user6@signupSignin.com",
                "signupSignin_password123"
        );
        assertEquals(newUser.getFirstName(), users.get(0).getFirstName());
        assertEquals(newUser.getLastName(), users.get(0).getLastName());
        assertEquals(newUser.getEmail(), users.get(0).getEmail());
        assertEquals(newUser.getPassword(), users.get(0).getPassword());
    }

    @Test
    public void changeSettingsFieldsInputEmailOnly() throws Exception {
        User user = new User(
                "signupSignin_user7_first",
                "signupSignin_user7_last",
                "user7@signupSignin.com",
                "signupSignin_password123"
        );
        userRepository.save(user);
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/settings")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("firstName", "")
                .param("lastName", "")
                .param("email", "user7new@signupSignin.com")
                .param("password", "")
                .param("confirmPassword", "")
                .param("oldPassword", "signupSignin_password123")
                .sessionAttr("user", user.getId())
        ).andExpect(redirectedUrl("/signin")).andExpect(status().is3xxRedirection());

        List<User> users = userRepository.findByEmail("user7new@signupSignin.com");
        assertEquals(users.size(), 1);
        User newUser = new User(
                "signupSignin_user7_first",
                "signupSignin_user7_last",
                "user7new@signupSignin.com",
                "signupSignin_password123"
        );
        assertEquals(newUser.getFirstName(), users.get(0).getFirstName());
        assertEquals(newUser.getLastName(), users.get(0).getLastName());
        assertEquals(newUser.getEmail(), users.get(0).getEmail());
        assertEquals(newUser.getPassword(), users.get(0).getPassword());

    }

    @Test
    public void changeSettingsFieldsInputPasswordOnly() throws Exception {
        User user = new User(
                "signupSignin_user8_first",
                "signupSignin_user8_last",
                "user8@signupSignin.com",
                "signupSignin_password123"
        );
        userRepository.save(user);
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/settings")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("firstName", "")
                .param("lastName", "")
                .param("email", "")
                .param("password", "signupSignin_password12345")
                .param("confirmPassword", "signupSignin_password12345")
                .param("oldPassword", "signupSignin_password123")
                .sessionAttr("user", user.getId())
        ).andDo(print());

        List<User> users = userRepository.findByEmail("user8@signupSignin.com");
        assertEquals(users.size(), 1);
        User newUser = new User(
                "signupSignin_user8_first",
                "signupSignin_user8_last",
                "user8@signupSignin.com",
                "signupSignin_password12345"
        );
        assertEquals(newUser.getFirstName(), users.get(0).getFirstName());
        assertEquals(newUser.getLastName(), users.get(0).getLastName());
        assertEquals(newUser.getEmail(), users.get(0).getEmail());
        assertEquals(newUser.getPassword(), users.get(0).getPassword());

    }


    @Test
    public void changeSettingsNoFieldsInput() throws Exception {
        User user = new User(
                "signupSignin_user4_first",
                "signupSignin_user4_last",
                "user4@signupSignin.com",
                "signupSignin_password123"
        );
        userRepository.save(user);
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/settings")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("firstName", "")
                .param("lastName", "")
                .param("email", "")
                .param("password", "")
                .param("confirmPassword", "")
                .param("oldPassword", "signupSignin_password123")
                .sessionAttr("user", user.getId())
        ).andExpect(content().string(containsString("DOCTYPE"))).andExpect(status().isOk());

        List<User> users = userRepository.findByEmail("user4@signupSignin.com");
        assertEquals(users.size(), 1);
        assertEquals(user.getFirstName(), users.get(0).getFirstName());
        assertEquals(user.getLastName(), users.get(0).getLastName());
        assertEquals(user.getEmail(), users.get(0).getEmail());
        assertEquals(user.getPassword(), users.get(0).getPassword());

    }

    @Test
    public void changeSettingsFieldsInputWithNoMeaningfulChange() throws Exception {
        User user = new User(
                "signupSignin_user5_first",
                "signupSignin_user5_last",
                "user5@signupSignin.com",
                "signupSignin_password123"
        );
        userRepository.save(user);
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/settings")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("firstName", "signupSignin_user5_first")
                .param("lastName", "signupSignin_user5_last")
                .param("email", "")
                .param("password", "")
                .param("confirmPassword", "")
                .param("oldPassword", "signupSignin_password123")
                .sessionAttr("user", user.getId())
        ).andExpect(content().string(containsString("DOCTYPE"))).andExpect(status().isOk());

        List<User> users = userRepository.findByEmail("user5@signupSignin.com");
        assertEquals(users.size(), 1);
        assertEquals(user.getFirstName(), users.get(0).getFirstName());
        assertEquals(user.getLastName(), users.get(0).getLastName());
        assertEquals(user.getEmail(), users.get(0).getEmail());
        assertEquals(user.getPassword(), users.get(0).getPassword());
    }


    @Test
    public void incorrectSignin() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/signin")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("email", "test1@test.com")
                .param("password", "test1234567")
        ).andExpect(content().string(containsString("DOCTYPE"))).andExpect(status().isOk()).andDo(print());
    }
}

