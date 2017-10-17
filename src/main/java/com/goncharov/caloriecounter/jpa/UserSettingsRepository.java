package com.goncharov.caloriecounter.jpa;

import com.goncharov.caloriecounter.domain.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Anton Goncharov
 */
@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings,Long> {
}
