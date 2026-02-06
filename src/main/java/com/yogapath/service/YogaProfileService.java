package com.yogapath.service;

import com.yogapath.dto.YogaProfileRequest;
import com.yogapath.dto.YogaProfileResponse;
import com.yogapath.model.Goal;
import com.yogapath.model.User;
import com.yogapath.model.YogaProfile;
import com.yogapath.repository.GoalRepository;
import com.yogapath.repository.UserRepository;
import com.yogapath.repository.YogaProfileRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class YogaProfileService {

    private final YogaProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;

    public YogaProfileService(YogaProfileRepository profileRepository,
                              UserRepository userRepository,
                              GoalRepository goalRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.goalRepository = goalRepository;
    }

    public YogaProfileResponse createProfile(YogaProfileRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        if (profileRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new RuntimeException("Profile already exists for user: " + request.getUserId());
        }

        YogaProfile profile = new YogaProfile();
        profile.setUser(user);
        profile.setWeeklyMinutesAvailable(request.getWeeklyMinutesAvailable());
        profile.setSessionsPerWeek(request.getSessionsPerWeek());
        profile.setDynamicPreference(request.getDynamicPreference());
        profile.setStructurePreference(request.getStructurePreference());
        profile.setPhilosophyOpenness(request.getPhilosophyOpenness());

        Set<Goal> goals = new HashSet<>(goalRepository.findAllById(request.getGoalIds()));
        profile.setGoals(goals);

        YogaProfile saved = profileRepository.save(profile);
        return toResponse(saved);
    }

    public YogaProfileResponse getProfileById(Long id) {
        YogaProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));
        return toResponse(profile);
    }

    public YogaProfileResponse getProfileByUserId(Long userId) {
        YogaProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + userId));
        return toResponse(profile);
    }

    public YogaProfileResponse updateProfile(Long id, YogaProfileRequest request) {
        YogaProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));

        profile.setWeeklyMinutesAvailable(request.getWeeklyMinutesAvailable());
        profile.setSessionsPerWeek(request.getSessionsPerWeek());
        profile.setDynamicPreference(request.getDynamicPreference());
        profile.setStructurePreference(request.getStructurePreference());
        profile.setPhilosophyOpenness(request.getPhilosophyOpenness());

        Set<Goal> goals = new HashSet<>(goalRepository.findAllById(request.getGoalIds()));
        profile.setGoals(goals);

        // updatedAt is automatically set by @UpdateTimestamp
        // YogaRecommendationService will compare this with recommendation.createdAt
        // to determine if new recommendation is needed

        YogaProfile saved = profileRepository.save(profile);
        return toResponse(saved);
    }

    private YogaProfileResponse toResponse(YogaProfile profile) {
        YogaProfileResponse response = new YogaProfileResponse();
        response.setId(profile.getId());
        response.setUserId(profile.getUser().getId());
        response.setWeeklyMinutesAvailable(profile.getWeeklyMinutesAvailable());
        response.setSessionsPerWeek(profile.getSessionsPerWeek());
        response.setDynamicPreference(profile.getDynamicPreference());
        response.setStructurePreference(profile.getStructurePreference());
        response.setPhilosophyOpenness(profile.getPhilosophyOpenness());
        response.setGoals(profile.getGoals());
        response.setCreatedAt(profile.getCreatedAt());
        response.setUpdatedAt(profile.getUpdatedAt());
        return response;
    }
}
