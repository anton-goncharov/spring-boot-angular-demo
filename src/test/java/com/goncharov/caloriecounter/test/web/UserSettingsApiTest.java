package com.goncharov.caloriecounter.test.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goncharov.caloriecounter.domain.User;
import com.goncharov.caloriecounter.jpa.UserRepository;
import com.goncharov.caloriecounter.test.TestData;
import com.goncharov.caloriecounter.test.TestUtil;
import com.goncharov.caloriecounter.web.dto.MealDTO;
import com.goncharov.caloriecounter.web.dto.UserSettingsDTO;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;

import javax.annotation.PostConstruct;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Anton Goncharov
 */
public class UserSettingsApiTest extends AbstractWebTest {

    private final Logger LOG = LoggerFactory.getLogger(UserSettingsApiTest.class);

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void beforeTests() {
        userRepository.deleteAll();

        userRepository.save(TestData.encodePassword(TestData.getUserSimple()));
    }

    @Test
    public void testUpdateAndGetSettings() throws Exception {
        // Login
        User user = TestData.getUserSimple();

        mockMvc.perform(get("/auth/account")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(user)))
                .andExpect(status().isOk());

        // Update settings
        UserSettingsDTO dto = new UserSettingsDTO();
        dto.setFirstName("Jon");
        dto.setLastName("Snow");
        dto.setExpectedCalories(500);
        dto.setProfileImageUrl(null);

        mockMvc.perform(fileUpload("/api/v1/usersettings") //it's a multipart request
                .file(new MockMultipartFile("test.jpg",new byte[255]))
                .param("firstName","Jon")
                .param("lastName","Snow")
                .param("expectedCalories","500")
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(requestAttrs)
                .with(credentials(user)))
                .andExpect(status().isOk());

        // Get settings
        MvcResult getUserSettings = mockMvc.perform(get("/api/v1/usersettings")
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(requestAttrs)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(user)))
                .andExpect(status().isOk())
                .andReturn();


        UserSettingsDTO userSettingsDTO = objectMapper.readValue(getUserSettings.getResponse().getContentAsString(), UserSettingsDTO.class);
        assertEquals(500, userSettingsDTO.getExpectedCalories().intValue());
        assertEquals("Jon", userSettingsDTO.getFirstName());
        assertEquals("Snow", userSettingsDTO.getLastName());

    }

}
