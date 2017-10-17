package com.goncharov.caloriecounter.security;

import com.goncharov.caloriecounter.domain.enums.Role;
import com.goncharov.caloriecounter.domain.User;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * @author Anton Goncharov
 */
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public CurrentUser(User user, String[] roles) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(roles));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public Set<Role> getRoles() {
        return user.getRoles();
    }

}
