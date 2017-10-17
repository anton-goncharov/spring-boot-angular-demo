package com.goncharov.caloriecounter.security;

import com.goncharov.caloriecounter.domain.User;
import com.goncharov.caloriecounter.domain.enums.Role;
import com.goncharov.caloriecounter.security.CurrentUser;
import com.goncharov.caloriecounter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/**
 * @author Anton Goncharov
 */
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email=%s was not found", email)));
        Set<Role> userRoles = user.getRoles();
        String[] roleNames = userRoles.stream().map(Enum::toString).toArray(size -> new String[size]);
        return new CurrentUser(user, roleNames);
    }

}
