package com.goncharov.caloriecounter.web.dto;

import com.goncharov.caloriecounter.domain.Meal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton Goncharov
 */
public class DayMealsDTO {

    private String date;
    private List<Meal> meals = new ArrayList<>();
    private Integer total;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "DayMealsDTO{" +
                "date='" + date + '\'' +
                ", meals=" + meals +
                ", total=" + total +
                '}';
    }
}
