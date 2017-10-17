package com.goncharov.caloriecounter.web.controller;

import com.goncharov.caloriecounter.domain.User;
import com.goncharov.caloriecounter.service.UserService;
import com.goncharov.caloriecounter.web.dto.ManagedUserDTO;
import com.goncharov.caloriecounter.web.dto.UserDTO;
import com.goncharov.caloriecounter.web.dto.UserRegistrationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/auth")
public class AccountController {

    private final Logger LOG = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private UserService userService;
    
    /**
     * GET  /account -> get the current user
     */
    @RequestMapping(value = "/account", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagedUserDTO> getAccount() {
        ResponseEntity<ManagedUserDTO> responseEntity = userService.getCurrentUser()
                .map(user -> new ResponseEntity<>(new ManagedUserDTO(user), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        return responseEntity;

    }

    /**
     * POST /register -> create new user
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerAccount(@Valid @RequestBody UserRegistrationDTO userDTO) {
        if (StringUtils.isEmpty(userDTO.getPassword())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return userService.getUserByEmail(userDTO.getUser().getEmail()).map((user) -> {
            // if user exists -> send error
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }).orElseGet(() -> {
            // create new user
            User user = new User();
            user.setEmail(userDTO.getUser().getEmail());
            user.setFirstName(userDTO.getUser().getFirstName());
            user.setLastName(userDTO.getUser().getLastName());
            user.setPassword(userDTO.getPassword());
            user = userService.createUser(user, Collections.emptySet());
            return new ResponseEntity<>(HttpStatus.CREATED);
        });
    }


}
