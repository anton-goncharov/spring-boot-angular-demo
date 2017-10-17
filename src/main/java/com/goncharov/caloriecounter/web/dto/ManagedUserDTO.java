package com.goncharov.caloriecounter.web.dto;

import com.goncharov.caloriecounter.domain.User;
import com.goncharov.caloriecounter.domain.enums.Role;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Anton Goncharov
 */
public class ManagedUserDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private Set<Role> assignedRole;

    public ManagedUserDTO() {
    }

    public ManagedUserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.isActive = user.isActive();
        this.assignedRole = user.getRoles();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Role> getAssignedRole() {
        return assignedRole;
    }

    public void setAssignedRole(Set<Role> assignedRole) {
        this.assignedRole = assignedRole;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "ManagedUserDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive=" + isActive +
                ", assignedRole=" + assignedRole +
                '}';
    }
}
