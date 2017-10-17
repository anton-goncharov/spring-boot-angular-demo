package com.goncharov.caloriecounter.test.web;

import com.goncharov.caloriecounter.domain.User;
import com.goncharov.caloriecounter.jpa.UserRepository;
import com.goncharov.caloriecounter.test.TestData;
import com.goncharov.caloriecounter.test.TestUtil;
import com.goncharov.caloriecounter.web.dto.UserDTO;
import com.goncharov.caloriecounter.web.dto.UserRegistrationDTO;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MvcResult;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Anton Goncharov
 */
public class AuthApiTest extends AbstractWebTest {

    private final Logger LOG = LoggerFactory.getLogger(AuthApiTest.class);

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void createTestData() {
        userRepository.deleteAll();

        userRepository.save(TestData.encodePassword(TestData.getUserSimple()));
        userRepository.save(TestData.encodePassword(TestData.getUserManager()));
        userRepository.save(TestData.encodePassword(TestData.getUserAdmin()));
        userRepository.save(TestData.encodePassword(TestData.getBlockedUser()));

    }

    @Test
    public void testRegistration() throws Exception {
        // Register new user
        UserRegistrationDTO dto = new UserRegistrationDTO();
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Kevin");
        userDTO.setLastName("Frost");
        userDTO.setEmail("new@mail.com");
        dto.setUser(userDTO);
        dto.setPassword("123");

        mockMvc.perform(post("/auth/register")
                .param("_csrf",csrfToken.getToken())
                .sessionAttrs(requestAttrs)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());

        // Login with created user
        User user = new User();
        user.setEmail("new@mail.com");
        user.setPassword("123");
        user.setActive(true);

        mockMvc.perform(get("/auth/account")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginAndGetUserDetails() throws Exception {
        User user = TestData.getUserSimple();

        mockMvc.perform(get("/auth/account")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginWithWrongCredentials() throws Exception {
        User user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("321");
        user.setActive(true);

        mockMvc.perform(get("/auth/account")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(user)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testLoginBlockedUser() throws Exception {
        User blockedUser = TestData.getBlockedUser();

        MvcResult mvcResult = mockMvc.perform(get("/auth/account")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(blockedUser)))
                .andReturn();
        LOG.info(mvcResult.getResponse().getContentAsString());

        mockMvc.perform(get("/auth/account")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(blockedUser)))
                .andExpect(status().isUnauthorized());

    }



}
