package com.goncharov.caloriecounter.security;

import com.goncharov.caloriecounter.security.exception.UserAccountBlockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Anton Goncharov
 */
@Component
public class AjaxAuthenticationFailureHandler  extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        if (exception instanceof UserAccountBlockedException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Account is blocked.");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Wrong credentials.");
        }

    }
}
