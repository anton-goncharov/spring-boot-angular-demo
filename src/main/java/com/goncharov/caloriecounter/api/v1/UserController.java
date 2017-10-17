package com.goncharov.caloriecounter.api.v1;

import com.goncharov.caloriecounter.security.RoleConstants;
import com.goncharov.caloriecounter.service.UserService;
import com.goncharov.caloriecounter.web.dto.ManagedUserDTO;
import com.goncharov.caloriecounter.web.dto.UserDTO;
import com.goncharov.caloriecounter.web.dto.UserSettingsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Anton Goncharov
 */
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(UserSettingsController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({RoleConstants.MANAGER, RoleConstants.ADMIN})
    public ResponseEntity<List<ManagedUserDTO>> getUsers() throws URISyntaxException {
        LOG.debug("REST request to get user list");
        List<ManagedUserDTO> result = userService.getAllUsersManaged();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({RoleConstants.MANAGER, RoleConstants.ADMIN})
    public ResponseEntity<ManagedUserDTO> update(@RequestBody ManagedUserDTO managedUserDTO, @PathVariable Long id) throws IOException {
        LOG.debug("REST request to update user: {}", managedUserDTO);
        try {
            ManagedUserDTO result = userService.updateUserManaged(managedUserDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(RoleConstants.ADMIN)
    public ResponseEntity<ManagedUserDTO> delete(@PathVariable Long id) throws IOException {
        LOG.debug("REST request to delete userId: {}", id);
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
