package com.goncharov.caloriecounter.service;

import com.goncharov.caloriecounter.domain.Meal;
import com.goncharov.caloriecounter.web.dto.DashboardFilterDTO;
import com.goncharov.caloriecounter.web.dto.DashboardMealListDTO;
import com.goncharov.caloriecounter.web.dto.MealDTO;

import java.util.List;
import java.util.Optional;

/**
 * @author Anton Goncharov
 */
public interface MealService {

    List<Meal> getAllMeals();

    Meal createMeal(MealDTO mealDTO);

    Meal updateMeal(MealDTO mealDTO);

    Meal deleteMeal(Long id);

    DashboardMealListDTO getDashboardMealList(int currentPage, int pageSizeInDays);

    DashboardMealListDTO getDashboardMealListFiltered(int page, int size, Optional<DashboardFilterDTO> filterDTO);

}
