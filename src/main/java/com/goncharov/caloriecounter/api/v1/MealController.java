package com.goncharov.caloriecounter.api.v1;

import com.goncharov.caloriecounter.domain.Meal;
import com.goncharov.caloriecounter.domain.enums.Role;
import com.goncharov.caloriecounter.security.RoleConstants;
import com.goncharov.caloriecounter.service.MealService;
import com.goncharov.caloriecounter.web.controller.AccountController;
import com.goncharov.caloriecounter.web.dto.DashboardFilterDTO;
import com.goncharov.caloriecounter.web.dto.DashboardMealListDTO;
import com.goncharov.caloriecounter.web.dto.MealDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Anton Goncharov
 */
@RestController
@RequestMapping("/api/v1")
public class MealController {

    private final Logger LOG = LoggerFactory.getLogger(MealController.class);

    @Autowired
    private MealService mealService;

    /**
     * GET /api/v1/meal -> get meals for current user
     */
    @RequestMapping(value = "/meal", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DashboardMealListDTO> getAllMeals(@RequestParam int page, @RequestParam int size) throws URISyntaxException {
        LOG.debug("REST request to get meal list, page = {}", page);
        DashboardMealListDTO dashboardMealList = mealService.getDashboardMealList(page, size);
        return new ResponseEntity<>(dashboardMealList, HttpStatus.OK);
    }

    /**
     * POST /api/v1/meal/filtered -> get meals for current user (with filter applied)
     */
    @RequestMapping(value = "/meal/filtered", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DashboardMealListDTO> getFilteredMeals(@RequestParam int page, @RequestParam int size,
                                                                 @RequestBody DashboardFilterDTO filterDTO) throws URISyntaxException {
        LOG.debug("REST request to get meal list with filter applied, filter = {}, page = {}", filterDTO, page);
        DashboardMealListDTO dashboardMealList = mealService.getDashboardMealListFiltered(page, size, Optional.ofNullable(filterDTO));
        return new ResponseEntity<>(dashboardMealList, HttpStatus.OK);
    }

    /**
     * POST /meal -> Create a new meal entry
     */
    @RequestMapping(value = "/meal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createMeal(@RequestBody MealDTO mealDTO)
            throws URISyntaxException {
        LOG.debug("REST request to save a new meal : {}", mealDTO);

        Meal meal = mealService.createMeal(mealDTO);
        ResponseEntity<Meal> responseEntity = null;
        try {
            responseEntity = ResponseEntity.created(new URI("/api/v1/meal/" + meal.getId()))
                    .body(meal);
        } catch (Exception e) {
            LOG.error("error create meal", e);
            responseEntity = new ResponseEntity<Meal>(HttpStatus.BAD_REQUEST);
        }
        // calculate amounts
        return responseEntity;
    }

    /**
     * POST /meal/{id} -> Update an existing meal entry
     */
    @RequestMapping(value = "/meal/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> updateMeal(@RequestBody MealDTO mealDTO, @PathVariable Long id)
            throws URISyntaxException {
        LOG.debug("REST request to update meal : {}", mealDTO);

        Meal meal = mealService.updateMeal(mealDTO);
        ResponseEntity<Meal> responseEntity = null;
        try {
            responseEntity = ResponseEntity.ok().body(meal);
        } catch (Exception e) {
            LOG.error("error create meal", e);
            responseEntity = new ResponseEntity<Meal>(HttpStatus.BAD_REQUEST);
        }
        // calculate amounts
        return responseEntity;
    }

    /**
     * DELETE /campaigns -> Delete an existing meal entry.
     */
    @RequestMapping(value = "/meal/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> deleteCampaign(@PathVariable Long id) throws URISyntaxException {
        LOG.debug("REST request to delete meal entry : {}", id);

        Meal deleted = mealService.deleteMeal(id);

        return new ResponseEntity<Meal>(deleted, HttpStatus.OK);
    }


}
