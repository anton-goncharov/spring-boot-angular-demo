package com.goncharov.caloriecounter.security;

import com.goncharov.caloriecounter.domain.User;
import com.goncharov.caloriecounter.domain.enums.Role;
import com.goncharov.caloriecounter.security.exception.UserAccountBlockedException;
import com.goncharov.caloriecounter.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Anton Goncharov
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final Logger LOG = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        User user = userService.getUserByEmail(username).get();

        if (user == null || !user.getEmail().equalsIgnoreCase(username)) {
            throw new BadCredentialsException("Username not found.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong password.");
        }

        if (!user.isActive()) {
            throw new UserAccountBlockedException("Account is blocked.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
