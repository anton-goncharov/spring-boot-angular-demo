package com.goncharov.caloriecounter.web.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton Goncharov
 */
public class DashboardMealListDTO {

    private Integer currentPage;
    private Integer totalPages;
    private List<DayMealsDTO> dayMealsList = new ArrayList<>();

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<DayMealsDTO> getDayMealsList() {
        return dayMealsList;
    }

    public void setDayMealsList(List<DayMealsDTO> dayMealsList) {
        this.dayMealsList = dayMealsList;
    }

    @Override
    public String toString() {
        return "DashboardMealListDTO{" +
                "currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", dayMealsList=" + dayMealsList +
                '}';
    }
}
