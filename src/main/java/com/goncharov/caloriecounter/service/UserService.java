package com.goncharov.caloriecounter.service;

import com.goncharov.caloriecounter.domain.User;
import com.goncharov.caloriecounter.domain.enums.Role;
import com.goncharov.caloriecounter.web.dto.ManagedUserDTO;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Anton Goncharov
 */
public interface UserService {

    Optional<User> getCurrentUser();

    Optional<User> getUserById(long id);

    Optional<User> getUserByEmail(String email);

    Collection<User> getAllUsers();

    User createUser(User user, Set<Role> rolesNames);

    User updateUserPassword(User user);

    List<ManagedUserDTO> getAllUsersManaged();

    ManagedUserDTO updateUserManaged(ManagedUserDTO managedUserDTO) throws Exception;

    void deleteUser(Long userId);
}
