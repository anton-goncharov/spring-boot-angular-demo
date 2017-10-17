package com.goncharov.caloriecounter.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    // forbid instantiating it
    private SecurityUtils() {
    }

    public static CurrentUser getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof CurrentUser) {
                CurrentUser principal = (CurrentUser) authentication.getPrincipal();
                return principal;
            }
        }
        throw new IllegalStateException("User not found!");
    }


}
