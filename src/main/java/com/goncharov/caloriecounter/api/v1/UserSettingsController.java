package com.goncharov.caloriecounter.api.v1;

import com.goncharov.caloriecounter.service.UserSettingsService;
import com.goncharov.caloriecounter.web.dto.DashboardMealListDTO;
import com.goncharov.caloriecounter.web.dto.UserSettingsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * @author Anton Goncharov
 */
@RestController
@RequestMapping("/api/v1")
public class UserSettingsController {

    private final Logger LOG = LoggerFactory.getLogger(UserSettingsController.class);

    @Autowired
    private UserSettingsService userSettingsService;

    @Value("${uploads.root}")
    private String uploadsRoot;

    /**
     * GET /api/v1/meal -> get meals for current user
     */
    @RequestMapping(value = "/usersettings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserSettingsDTO> getUserSettings() throws URISyntaxException {
        LOG.debug("REST request to get user settings");
        UserSettingsDTO userSettingsDTO = userSettingsService.getUserSettings();
        return new ResponseEntity<>(userSettingsDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/usersettings", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity upload(@RequestParam(value = "file", required = false) MultipartFile file, UserSettingsDTO settings) throws IOException {
        LOG.debug("REST request to update user settings : {}", settings);

        //store file in storage
        File saveDir = new File(uploadsRoot + "uploads");
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }

        String relativePath = null;
        if ((file != null) && !file.isEmpty()) {
            LOG.debug("Uploaded filename : {}", file.getOriginalFilename());
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            relativePath = "uploads/" + UUID.randomUUID().toString() + extension;
            String filepath = uploadsRoot + relativePath;
            File savedImage = new File(filepath);
            if (savedImage.createNewFile()) {
                file.transferTo(savedImage);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        userSettingsService.updateSettings(settings, relativePath);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


}
