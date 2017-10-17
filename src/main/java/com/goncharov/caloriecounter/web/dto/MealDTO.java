package com.goncharov.caloriecounter.web.dto;

import javax.validation.constraints.NotNull;

/**
 * @author Anton Goncharov
 */
public class MealDTO {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer calories;

    private String date;
    private String time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "MealDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", calories=" + calories +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

}
