package com.goncharov.caloriecounter.service;

import com.goncharov.caloriecounter.web.dto.UserSettingsDTO;

/**
 * @author Anton Goncharov
 */
public interface UserSettingsService {

    void updateSettings(UserSettingsDTO settings, String relativePath);

    UserSettingsDTO getUserSettings();
}
