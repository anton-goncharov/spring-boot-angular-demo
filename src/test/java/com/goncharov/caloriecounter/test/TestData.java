package com.goncharov.caloriecounter.test;

import com.goncharov.caloriecounter.domain.User;
import com.goncharov.caloriecounter.domain.enums.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Anton Goncharov
 */
public class TestData {

    private static User userSimple = createUser();
    private static User userManager = createManager();
    private static User userAdmin = createAdmin();
    private static User blockedUser = createBlocked();

    public static User createUser() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        return new User("User", "Doe", true, "user@mail.com", "123", roles);
    }

    public static User createBlocked() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        return new User("John", "Doe", false, "blocked@mail.com", "123", roles);
    }

    public static User createManager() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        roles.add(Role.ROLE_MANAGER);
        return new User("Sarah","Black",true,"man@mail.com","123", roles);
    }

    public static User createAdmin() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        roles.add(Role.ROLE_ADMIN);
        return new User("Robert","Smith",true,"admin@mail.com","123", roles);
    }

    public static User encodePassword(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return new User(user.getFirstName(), user.getLastName(), user.isActive(), user.getEmail(), bCryptPasswordEncoder.encode(user.getPassword()), user.getRoles());
    }

    public static User getUserSimple() {
        return userSimple;
    }

    public static User getUserManager() {
        return userManager;
    }

    public static User getUserAdmin() {
        return userAdmin;
    }

    public static User getBlockedUser() {
        return blockedUser;
    }
}
