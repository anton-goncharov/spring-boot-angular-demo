package com.goncharov.caloriecounter.jpa;

import com.goncharov.caloriecounter.domain.Meal;
import com.goncharov.caloriecounter.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Anton Goncharov
 */
@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    Page<Meal> findByUserAndMealDateBefore(User user, Date mealDate, Pageable pageable);

    @Query(value = "SELECT m FROM Meal m WHERE m.user = ?1 " +
            "AND m.mealDate < ?2 " +
            "AND ((date(m.mealDate) >= date(?3)) AND (date(m.mealDate) <= date(?4))) " +
            "AND ((time(m.mealDate) >= time(?5)) AND (time(m.mealDate) <= time(?6))) " +
            "ORDER BY m.mealDate DESC")
    Page<Meal> findByUserAndMealDateBeforeFiltered(User user, Date mealDate,
                                                   Timestamp startDate, Timestamp endDate,
                                                   Timestamp startTime, Timestamp endTime,
                                                   Pageable pageable);

    @Query(value = "SELECT DISTINCT(date(m.mealDate)) FROM Meal m WHERE m.user = ?1 ORDER BY m.mealDate DESC")
    List<Date> findDistinctMealDateByUser(User user);

    @Query(value = "SELECT DISTINCT(date(m.mealDate)) FROM Meal m WHERE m.user = ?1 AND m.mealDate BETWEEN ?2 AND ?3 ORDER BY m.mealDate DESC")
    List<Date> findDistinctMealDateByUser(User user, Timestamp timestampFrom, Timestamp timestampTo);
}
