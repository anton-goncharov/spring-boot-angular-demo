package com.goncharov.caloriecounter.test.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goncharov.caloriecounter.domain.Meal;
import com.goncharov.caloriecounter.domain.User;
import com.goncharov.caloriecounter.jpa.MealRepository;
import com.goncharov.caloriecounter.jpa.UserRepository;
import com.goncharov.caloriecounter.test.TestData;
import com.goncharov.caloriecounter.test.TestUtil;
import com.goncharov.caloriecounter.web.dto.DashboardMealListDTO;
import com.goncharov.caloriecounter.web.dto.DayMealsDTO;
import com.goncharov.caloriecounter.web.dto.MealDTO;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Anton Goncharov
 */
public class MealApiTest extends AbstractWebTest {

    private final Logger LOG = LoggerFactory.getLogger(MealApiTest.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MealRepository mealRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void createTestData() {
        mealRepository.deleteAll();
        userRepository.deleteAll();

        userRepository.save(TestData.encodePassword(TestData.getUserSimple()));
    }

    @Test
    public void testCrudMeal() throws Exception {
        // Login
        User user = TestData.getUserSimple();

        mockMvc.perform(get("/auth/account")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(user)))
                .andExpect(status().isOk());

        // Create meal
        MealDTO dto = new MealDTO();
        dto.setName("Carrot");
        dto.setCalories(100);
        dto.setDate(DateTimeFormatter.ISO_INSTANT.format(Instant.now()));
        dto.setTime(DateTimeFormatter.ISO_INSTANT.format(Instant.now()));

        MvcResult createResult = mockMvc.perform(post("/api/v1/meal")
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(requestAttrs)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(user)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = createResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(response);
        long id = jsonNode.get("id").longValue();

        // Get user meals
        MvcResult getAllResult = mockMvc.perform(get("/api/v1/meal")
                .param("_csrf", csrfToken.getToken())
                .param("page","0")
                .param("size","10")
                .sessionAttrs(requestAttrs)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(user)))
                .andExpect(status().isOk())
                .andReturn();

        response = getAllResult.getResponse().getContentAsString();
        DashboardMealListDTO mealListDTO = objectMapper.readValue(response, DashboardMealListDTO.class);
        assertEquals(1, mealListDTO.getDayMealsList().size());

        DayMealsDTO dayMealsDTO = mealListDTO.getDayMealsList().get(0);
        assertEquals(1, dayMealsDTO.getMeals().size());

        Meal meal = dayMealsDTO.getMeals().get(0);
        assertEquals(id, meal.getId().intValue());
        assertEquals(100, meal.getCalories().intValue());
        assertEquals("Carrot", meal.getName());

        //Edit meal
        dto = new MealDTO();
        dto.setId(id);
        dto.setName("Cabbage");
        dto.setCalories(200);
        mockMvc.perform(post("/api/v1/meal/" + id)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(requestAttrs)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(user)))
                .andExpect(status().isOk());

        // cheack via jpa (get method is already tested)
        Meal mealEdited = mealRepository.findOne(id);
        assertEquals("Cabbage", mealEdited.getName());
        assertEquals(200, mealEdited.getCalories().intValue());

        //Delete meal
        mockMvc.perform(delete("/api/v1/meal/" + id)
                .param("_csrf", csrfToken.getToken())
                .sessionAttrs(requestAttrs)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .with(credentials(user)))
                .andExpect(status().isOk());

        assertFalse(mealRepository.exists(id));
    }

}
