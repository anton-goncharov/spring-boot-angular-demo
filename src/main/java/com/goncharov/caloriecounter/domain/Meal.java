package com.goncharov.caloriecounter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.goncharov.caloriecounter.security.SecurityUtils;
import com.goncharov.caloriecounter.web.dto.MealDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * @author Anton Goncharov
 */
@Entity
@Table(name = "meal")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer calories;

    @NotNull
    private Timestamp mealDate;

    private Timestamp updated;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public Meal() {
    }

    public Meal(MealDTO mealDTO) {
        this.setName(mealDTO.getName());
        this.setCalories(mealDTO.getCalories());
        String date = mealDTO.getDate().split("T")[0];
        String time = mealDTO.getTime().split("T")[1];
        Instant dateTime = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(date + "T" + time));
        this.setMealDate(Timestamp.from(dateTime));
        this.setUpdated(Timestamp.from(Instant.now()));
        this.setUser(SecurityUtils.getCurrentUser().getUser());
    }

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

    public Timestamp getMealDate() {
        return mealDate;
    }

    public void setMealDate(Timestamp mealDate) {
        this.mealDate = mealDate;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", calories=" + calories +
                ", mealDate=" + mealDate +
                ", updated=" + updated +
                '}';
    }
}
