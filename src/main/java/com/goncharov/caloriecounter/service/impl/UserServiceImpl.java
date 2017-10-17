package com.goncharov.caloriecounter.service.impl;

import com.goncharov.caloriecounter.domain.User;
import com.goncharov.caloriecounter.domain.enums.Role;
import com.goncharov.caloriecounter.jpa.UserRepository;
import com.goncharov.caloriecounter.security.SecurityUtils;
import com.goncharov.caloriecounter.service.UserService;
import com.goncharov.caloriecounter.web.dto.ManagedUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Anton Goncharov
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getCurrentUser() {
        return userRepository.findOneByEmail(SecurityUtils.getCurrentUser().getUsername());
    }

    @Override
    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(userRepository.findOne(id));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll(new Sort("email"));
    }

    @Override
    public User createUser(User user, Set<Role> rolesNames) {
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        user.setRoles(roles);
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        LOG.debug("User created : {}", user.getEmail());
        return user;
    }

    @Override
    public User updateUserPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        LOG.debug("User password updated : {}", user.getEmail());
        return user;
    }

    /* User Management Methods */

    @Override
    public List<ManagedUserDTO> getAllUsersManaged() {
        List<User> allUsers = userRepository.findAll();
        List<ManagedUserDTO> dtos = allUsers.stream().map(ManagedUserDTO::new).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public ManagedUserDTO updateUserManaged(ManagedUserDTO managedUserDTO) throws Exception {
        boolean isAdmin = SecurityUtils.getCurrentUser().getRoles().contains(Role.ROLE_ADMIN);

        User user = userRepository.findOne(managedUserDTO.getId());
        if (!user.getRoles().equals(managedUserDTO.getAssignedRole())) {
            if (isAdmin) {
                user.setRoles(managedUserDTO.getAssignedRole());
            } else {
                // throw Unauthorized
                throw new Exception("User must have ADMIN role to change other users roles.");
            }
        }

        if (user.isActive() != managedUserDTO.isActive()) {
            user.setActive(managedUserDTO.isActive());
        }

        user = userRepository.save(user);

        return new ManagedUserDTO(user);
    }

    @Override
    public void deleteUser(Long userId) {
        if (userRepository.exists(userId)) {
            userRepository.delete(userId);
        }
    }


}
