package com.goncharov.caloriecounter.web.dto;

import javax.validation.constraints.Size;

/**
 * @author Anton Goncharov
 */
public class UserRegistrationDTO {

    private UserDTO user;
    @Size(max=100)
    private String password;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserRegistrationDTO{" +
                "user=" + user +
                ", password='" + password + '\'' +
                '}';
    }

}
