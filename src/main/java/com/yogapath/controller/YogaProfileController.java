package com.yogapath.controller;

import com.yogapath.dto.YogaProfileRequest;
import com.yogapath.dto.YogaProfileResponse;
import com.yogapath.service.YogaProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class YogaProfileController {

    private final YogaProfileService profileService;

    public YogaProfileController(YogaProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public ResponseEntity<YogaProfileResponse> createProfile(@Valid @RequestBody YogaProfileRequest request) {
        YogaProfileResponse response = profileService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public YogaProfileResponse getProfileById(@PathVariable Long id) {
        return profileService.getProfileById(id);
    }

    @GetMapping("/user/{userId}")
    public YogaProfileResponse getProfileByUserId(@PathVariable Long userId) {
        return profileService.getProfileByUserId(userId);
    }

    @PutMapping("/{id}")
    public YogaProfileResponse updateProfile(@PathVariable Long id, @Valid @RequestBody YogaProfileRequest request) {
        return profileService.updateProfile(id, request);
    }
}
