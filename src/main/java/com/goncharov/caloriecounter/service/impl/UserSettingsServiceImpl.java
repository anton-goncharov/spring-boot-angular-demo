package com.goncharov.caloriecounter.service.impl;

import com.goncharov.caloriecounter.domain.User;
import com.goncharov.caloriecounter.domain.UserSettings;
import com.goncharov.caloriecounter.jpa.UserRepository;
import com.goncharov.caloriecounter.jpa.UserSettingsRepository;
import com.goncharov.caloriecounter.security.SecurityUtils;
import com.goncharov.caloriecounter.service.UserSettingsService;
import com.goncharov.caloriecounter.web.dto.UserSettingsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Anton Goncharov
 */
@Service
@Transactional
public class UserSettingsServiceImpl implements UserSettingsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void updateSettings(UserSettingsDTO settings, String relativePath) {
        User user = userRepository.findOne(SecurityUtils.getCurrentUser().getId());
        UserSettings userSettings = user.getUserSettings();
        if (userSettings == null) {
            userSettings = new UserSettings();
            userSettings.setUser(user);
            user.setUserSettings(userSettings);
        }

        String firstName = settings.getFirstName();
        String lastName = settings.getLastName();
        Integer expectedCalories = settings.getExpectedCalories();

        if (!firstName.equals(user.getFirstName())) {
            user.setFirstName(firstName);
        }

        if (!lastName.equals(user.getLastName())) {
            user.setLastName(lastName);
        }

        if (settings.getExpectedCalories() != -1) {
            if ((userSettings.getCaloriesExpectedDaily() == null) || !userSettings.getCaloriesExpectedDaily().equals(settings.getExpectedCalories())) {
                userSettings.setCaloriesExpectedDaily(expectedCalories);
            }
        }

        if (relativePath != null) {
            userSettings.setProfileImageUrl(relativePath);
        }
        userRepository.save(user);
    }

    @Override
    public UserSettingsDTO getUserSettings() {
        User user = userRepository.findOne(SecurityUtils.getCurrentUser().getId());
        UserSettings userSettings = user.getUserSettings();
        return new UserSettingsDTO(user, userSettings);

    }

}
