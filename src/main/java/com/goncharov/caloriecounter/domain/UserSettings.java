package com.goncharov.caloriecounter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * @author Anton Goncharov
 */
@Entity
@Table(name = "user_settings")
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private Integer caloriesExpectedDaily;

    private String profileImageUrl;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public Integer getCaloriesExpectedDaily() {
        return caloriesExpectedDaily;
    }

    public void setCaloriesExpectedDaily(Integer caloriesExpectedDaily) {
        this.caloriesExpectedDaily = caloriesExpectedDaily;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "caloriesExpectedDaily=" + caloriesExpectedDaily +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }
}
