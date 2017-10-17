package com.goncharov.caloriecounter.service.impl;

import com.goncharov.caloriecounter.api.v1.MealController;
import com.goncharov.caloriecounter.domain.Meal;
import com.goncharov.caloriecounter.domain.User;
import com.goncharov.caloriecounter.jpa.MealRepository;
import com.goncharov.caloriecounter.jpa.UserRepository;
import com.goncharov.caloriecounter.security.CurrentUser;
import com.goncharov.caloriecounter.security.SecurityUtils;
import com.goncharov.caloriecounter.service.MealService;
import com.goncharov.caloriecounter.web.dto.DashboardFilterDTO;
import com.goncharov.caloriecounter.web.dto.DashboardMealListDTO;
import com.goncharov.caloriecounter.web.dto.DayMealsDTO;
import com.goncharov.caloriecounter.web.dto.MealDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.*;

/**
 * @author Anton Goncharov
 */
@Service
@Transactional
public class MealServiceImpl implements MealService {

    private final Logger LOG = LoggerFactory.getLogger(MealServiceImpl.class);

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Meal> getAllMeals() {
        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        User user = userRepository.findOne(currentUser.getId());
        return user.getMeals();
    }

    @Override
    public Meal createMeal(MealDTO mealDTO) {
        Meal meal = new Meal(mealDTO);
        return mealRepository.save(meal);
    }

    @Override
    public Meal updateMeal(MealDTO mealDTO) {
        Meal meal = mealRepository.findOne(mealDTO.getId());
        if (!mealDTO.getCalories().equals(meal.getCalories())) {
            meal.setCalories(mealDTO.getCalories());
        }
        if (!mealDTO.getName().equals(meal.getName())) {
            meal.setName(mealDTO.getName());
        }
        meal.setUpdated(Timestamp.from(Instant.now()));
        return mealRepository.save(meal);
    }

    @Override
    public Meal deleteMeal(Long id) {
        Meal meal = mealRepository.findOne(id);
        mealRepository.delete(meal);
        return meal;
    }

    @Override
    public DashboardMealListDTO getDashboardMealListFiltered(int currentPage, int pageSizeInDays,
                                                             Optional<DashboardFilterDTO> filterDTO) {
        DashboardMealListDTO result = new DashboardMealListDTO();


        Timestamp dateFrom = null,
                dateTo = null,
                timeFrom = null,
                timeTo = null;
        if (filterDTO.isPresent()) {
            DashboardFilterDTO dto = filterDTO.get();
            dateFrom = Timestamp.from(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(dto.getFromDate())));
            dateTo = Timestamp.from(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(dto.getToDate())));
            timeFrom = Timestamp.from(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(dto.getFromTime())));
            timeTo = Timestamp.from(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(dto.getToTime())));
        }

        CurrentUser currentUser = SecurityUtils.getCurrentUser();
        User user = userRepository.findOne(currentUser.getId());

        // --------------- get total number of pages
        List<Date> days;
        if (filterDTO.isPresent()) {
            days = mealRepository.findDistinctMealDateByUser(user, dateFrom, dateTo);
        } else {
            days = mealRepository.findDistinctMealDateByUser(user);
        }
        result.setTotalPages((int) Math.ceil((double)days.size() / (double)pageSizeInDays));
        Date startDate;
        if (currentPage == 0) {
            startDate = Date.valueOf(LocalDate.now().plusDays(1));
        } else {
            Date requestDate = days.get(pageSizeInDays * currentPage);
            startDate = Date.valueOf(requestDate.toLocalDate().plusDays(1));
        }

        // --------------- get meal list for dashboard
        int page = 0;
        int batchSize = 5;
        DayMealsDTO currentDay = new DayMealsDTO();
        // group result by days, iterate query until the list is populated
        Page<Meal> meals;
        if (filterDTO.isPresent()) {
            meals = mealRepository.findByUserAndMealDateBeforeFiltered(user, startDate, dateFrom, dateTo, timeFrom, timeTo,
                    new PageRequest(page, batchSize, new Sort(new Order(Direction.DESC, "mealDate"))));
        } else {
            meals = mealRepository.findByUserAndMealDateBefore(user, startDate,
                    new PageRequest(page, batchSize, new Sort(new Order(Direction.DESC, "mealDate"))));
        }
        while ((result.getDayMealsList().size() < pageSizeInDays)) {
            for (Meal meal : meals) {
                String date;
                LocalDate mealDate = meal.getMealDate().toLocalDateTime().toLocalDate();
                if (mealDate.equals(LocalDate.now())) {
                    date = "Today";
                } else if (mealDate.equals(LocalDate.now().minusDays(1))) {
                    date = "Yesterday";
                } else {
                    date = DateTimeFormatter.ofPattern("MM/dd/yyyy").format(mealDate);
                }
                if (currentDay.getMeals().isEmpty()) {
                    // it's the first meal in this day
                    currentDay.setDate(date);
                    currentDay.setTotal(meal.getCalories());
                } else if (currentDay.getDate().equals(date)) {
                    // it's another meal in the same day
                    currentDay.setTotal(currentDay.getTotal() + meal.getCalories());
                } else if (!currentDay.getDate().equals(date)) {
                    // it's the next day - save the current day and start a new one
                    result.getDayMealsList().add(currentDay);
                    currentDay = null;
                    if (result.getDayMealsList().size() < pageSizeInDays) {
                        currentDay = new DayMealsDTO();
                        currentDay.setDate(date);
                        currentDay.setTotal(meal.getCalories());
                    } else {
                        break;
                    }
                }
                currentDay.getMeals().add(meal);
            }

            // proceed to the next page of the result set
            if (meals.hasNext()) {
                if (filterDTO.isPresent()) {
                    meals = mealRepository.findByUserAndMealDateBeforeFiltered(user, startDate,
                            dateFrom, dateTo, timeFrom, timeTo, meals.nextPageable());
                } else {
                    meals = mealRepository.findByUserAndMealDateBefore(user, startDate, meals.nextPageable());
                }
            } else {
                // flush the current day object if it's not added yet
                if (currentDay != null) {
                    result.getDayMealsList().add(currentDay);
                }
                break;
            }
        }

        return result;
    }

    @Override
    public DashboardMealListDTO getDashboardMealList(int currentPage, int pageSizeInDays) {
        return getDashboardMealListFiltered(currentPage, pageSizeInDays, Optional.empty());
    }
}
