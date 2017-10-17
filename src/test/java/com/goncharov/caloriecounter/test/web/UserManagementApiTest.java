package com.goncharov.caloriecounter.test.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goncharov.caloriecounter.domain.User;
import com.goncharov.caloriecounter.domain.enums.Role;
import com.goncharov.caloriecounter.jpa.UserRepository;
import com.goncharov.caloriecounter.test.TestData;
import com.goncharov.caloriecounter.test.TestUtil;
import com.goncharov.caloriecounter.web.dto.ManagedUserDTO;
import com.goncharov.caloriecounter.web.dto.UserSettingsDTO;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;

import javax.annotation.PostConstruct;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Anton Goncharov
 */
public class UserManagementApiTest extends AbstractWebTest {

    private final Logger LOG = LoggerFactory.getLogger(UserManagementApiTest.class);

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();
    private long simpleUserId;

    @PostConstruct
    public void beforeTests() {
        userRepository.deleteAll();

        User saved = userRepository.save(TestData.encodePassword(TestData.getUserSimple()));
        simpleUserId = saved.getId();
        userRepository.save(TestData.encodePassword(TestData.getUserManager()));
        userRepository.save(TestData.encodePassword(TestData.getUserAdmin()));
    }

    @Test
    public void testManageWithUserRole() throws Exception {
        User user = TestData.getUserSimple();

        mockMvc.perform(get("/api/v1/user")
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(requestAttrs)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testManageWithManagerRole() throws Exception {
        User manager = TestData.getUserManager();

        // Get users data
        MvcResult result = mockMvc.perform(get("/api/v1/user")
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(requestAttrs)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(manager)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(jsonNode.isArray());
        assertEquals(3,jsonNode.size());

        // Block active user
        ManagedUserDTO dto = new ManagedUserDTO();
        dto.setId(simpleUserId);
        dto.setActive(false);
        dto.setAssignedRole(TestData.getUserSimple().getRoles());

        mockMvc.perform(post("/api/v1/user/" + simpleUserId)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(requestAttrs)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(manager)))
                .andExpect(status().isOk());

        User user = userRepository.findOne(simpleUserId);
        assertFalse(user.isActive());

        // Try change role
        dto = new ManagedUserDTO();
        dto.setId(simpleUserId);
        dto.setActive(false);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        roles.add(Role.ROLE_ADMIN);
        dto.setAssignedRole(roles);

        mockMvc.perform(post("/api/v1/user/" + simpleUserId)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(requestAttrs)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(manager)))
                .andExpect(status().isForbidden());

        // Delete user
        mockMvc.perform(delete("/api/v1/user/" + simpleUserId)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(requestAttrs)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(manager)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testManageWithAdminRole() throws Exception {
        User admin = TestData.getUserAdmin();

        // Change role
        ManagedUserDTO dto = new ManagedUserDTO();
        dto.setId(simpleUserId);
        dto.setActive(false);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        roles.add(Role.ROLE_ADMIN);
        dto.setAssignedRole(roles);

        mockMvc.perform(post("/api/v1/user/" + simpleUserId)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(requestAttrs)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(admin)))
                .andExpect(status().isOk());

        // Delete user
        mockMvc.perform(delete("/api/v1/user/" + simpleUserId)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(requestAttrs)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(admin)))
                .andExpect(status().isOk());

    }



}
