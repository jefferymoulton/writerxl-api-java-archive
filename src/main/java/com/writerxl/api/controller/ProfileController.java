package com.writerxl.api.controller;

import com.writerxl.api.ProfileNotFoundException;
import com.writerxl.api.model.Profile;
import com.writerxl.api.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<Profile> createProfile(@RequestBody Profile profile) {
        try {
            return ResponseEntity.ok(profileService.createProfile(profile));
        }
        catch (DuplicateKeyException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The user account already exists.", ex);
        }
    }

    @GetMapping(path = "/key/{key}")
    public ResponseEntity<Profile> getProfileByKey(@PathVariable String key) {
        try {
            return ResponseEntity.ok(profileService.getProfileByKey(key));
        }
        catch (ProfileNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find specified user.", ex);
        }
    }

    @GetMapping(path = "/email/{email:.+}")
    public ResponseEntity<Profile> getProfileByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok(profileService.getProfileByEmail(email));
        }
        catch (ProfileNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find specified user.", ex);
        }
    }
}
