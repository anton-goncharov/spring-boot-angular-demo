package com.goncharov.caloriecounter.web.dto;

import com.goncharov.caloriecounter.domain.User;
import com.goncharov.caloriecounter.domain.UserSettings;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Null;

/**
 * @author Anton Goncharov
 */
public class UserSettingsDTO {

    private String firstName;
    private String lastName;
    private Integer expectedCalories;
    private String profileImageUrl;

    public UserSettingsDTO() {
    }

    public UserSettingsDTO(User user, UserSettings userSettings) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        if (userSettings != null) {
            this.expectedCalories = userSettings.getCaloriesExpectedDaily();
            this.profileImageUrl = userSettings.getProfileImageUrl();
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getExpectedCalories() {
        return expectedCalories;
    }

    public void setExpectedCalories(Integer expectedCalories) {
        this.expectedCalories = expectedCalories;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public String toString() {
        return "UserSettingsDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", expectedCalories=" + expectedCalories +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }
}
