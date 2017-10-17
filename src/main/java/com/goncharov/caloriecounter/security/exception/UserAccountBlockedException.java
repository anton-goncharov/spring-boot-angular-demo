package com.goncharov.caloriecounter.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Anton Goncharov
 */
public class UserAccountBlockedException extends AuthenticationException {

    public UserAccountBlockedException(String msg) {
        super(msg);
    }

}
