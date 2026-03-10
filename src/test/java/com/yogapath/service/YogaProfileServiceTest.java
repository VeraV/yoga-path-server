package com.yogapath.service;

import com.yogapath.dto.YogaProfileRequest;
import com.yogapath.dto.YogaProfileResponse;
import com.yogapath.model.Goal;
import com.yogapath.model.User;
import com.yogapath.model.YogaProfile;
import com.yogapath.model.enums.DynamicPreference;
import com.yogapath.model.enums.PhilosophyOpenness;
import com.yogapath.model.enums.StructurePreference;
import com.yogapath.repository.GoalRepository;
import com.yogapath.repository.UserRepository;
import com.yogapath.repository.YogaProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class YogaProfileServiceTest {

    @Mock
    private YogaProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private YogaProfileService yogaProfileService;

    private User user;
    private Goal goal1;
    private Goal goal2;
    private YogaProfile profile;
    private YogaProfileRequest request;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Vera", "vera@example.com", "hashed", true);

        goal1 = new Goal(1L, "Flexibility", "Improve flexibility", null);
        goal2 = new Goal(2L, "Strength", "Build core strength", null);

        profile = new YogaProfile();
        profile.setId(1L);
        profile.setUser(user);
        profile.setWeeklyMinutesAvailable(120);
        profile.setSessionsPerWeek(3);
        profile.setDynamicPreference(DynamicPreference.DYNAMIC);
        profile.setStructurePreference(StructurePreference.STRUCTURED);
        profile.setPhilosophyOpenness(PhilosophyOpenness.OPEN);
        profile.setGoals(Set.of(goal1, goal2));

        request = new YogaProfileRequest();
        request.setUserId(1L);
        request.setWeeklyMinutesAvailable(120);
        request.setSessionsPerWeek(3);
        request.setDynamicPreference(DynamicPreference.DYNAMIC);
        request.setStructurePreference(StructurePreference.STRUCTURED);
        request.setPhilosophyOpenness(PhilosophyOpenness.OPEN);
        request.setGoalIds(Set.of(1L, 2L));
    }

    // --- createProfile ---

    @Test
    void createProfile_throwsException_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> yogaProfileService.createProfile(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found with id: 1");

        verify(profileRepository, never()).save(any());
    }

    @Test
    void createProfile_throwsException_whenProfileAlreadyExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));

        assertThatThrownBy(() -> yogaProfileService.createProfile(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Profile already exists for user: 1");

        verify(profileRepository, never()).save(any());
    }

    @Test
    void createProfile_savesAndReturnsResponse_whenValid() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(goalRepository.findAllById(Set.of(1L, 2L))).thenReturn(List.of(goal1, goal2));
        when(profileRepository.save(any(YogaProfile.class))).thenReturn(profile);

        YogaProfileResponse result = yogaProfileService.createProfile(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getWeeklyMinutesAvailable()).isEqualTo(120);
        assertThat(result.getSessionsPerWeek()).isEqualTo(3);
        assertThat(result.getDynamicPreference()).isEqualTo(DynamicPreference.DYNAMIC);
    }

    @Test
    void createProfile_savesWithEmptyGoals_whenGoalIdsIsNull() {
        request.setGoalIds(null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.empty());

        profile.setGoals(Set.of());
        when(profileRepository.save(any(YogaProfile.class))).thenReturn(profile);

        YogaProfileResponse result = yogaProfileService.createProfile(request);

        verify(goalRepository, never()).findAllById(any());
        assertThat(result.getGoals()).isEmpty();
    }

    @Test
    void createProfile_savesWithEmptyGoals_whenGoalIdsIsEmpty() {
        request.setGoalIds(Set.of());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.empty());

        profile.setGoals(Set.of());
        when(profileRepository.save(any(YogaProfile.class))).thenReturn(profile);

        YogaProfileResponse result = yogaProfileService.createProfile(request);

        verify(goalRepository, never()).findAllById(any());
        assertThat(result.getGoals()).isEmpty();
    }

    // --- getProfileById ---

    @Test
    void getProfileById_returnsResponse_whenProfileExists() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        YogaProfileResponse result = yogaProfileService.getProfileById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(1L);
    }

    @Test
    void getProfileById_throwsException_whenProfileNotFound() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> yogaProfileService.getProfileById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Profile not found with id: 99");
    }

    // --- getProfileByUserId ---

    @Test
    void getProfileByUserId_returnsResponse_whenProfileExists() {
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));

        YogaProfileResponse result = yogaProfileService.getProfileByUserId(1L);

        assertThat(result.getUserId()).isEqualTo(1L);
    }

    @Test
    void getProfileByUserId_throwsException_whenProfileNotFound() {
        when(profileRepository.findByUserId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> yogaProfileService.getProfileByUserId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Profile not found for user: 99");
    }

    // --- updateProfile ---

    @Test
    void updateProfile_throwsException_whenProfileNotFound() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> yogaProfileService.updateProfile(99L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Profile not found with id: 99");

        verify(profileRepository, never()).save(any());
    }

    @Test
    void updateProfile_updatesFieldsAndReturnsResponse() {
        YogaProfileRequest updateRequest = new YogaProfileRequest();
        updateRequest.setUserId(1L);
        updateRequest.setWeeklyMinutesAvailable(180);
        updateRequest.setSessionsPerWeek(5);
        updateRequest.setDynamicPreference(DynamicPreference.STATIC);
        updateRequest.setStructurePreference(StructurePreference.CREATIVE);
        updateRequest.setPhilosophyOpenness(PhilosophyOpenness.NOT_OPEN);
        updateRequest.setGoalIds(Set.of(1L));

        YogaProfile updatedProfile = new YogaProfile();
        updatedProfile.setId(1L);
        updatedProfile.setUser(user);
        updatedProfile.setWeeklyMinutesAvailable(180);
        updatedProfile.setSessionsPerWeek(5);
        updatedProfile.setDynamicPreference(DynamicPreference.STATIC);
        updatedProfile.setStructurePreference(StructurePreference.CREATIVE);
        updatedProfile.setPhilosophyOpenness(PhilosophyOpenness.NOT_OPEN);
        updatedProfile.setGoals(Set.of(goal1));

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(goalRepository.findAllById(Set.of(1L))).thenReturn(List.of(goal1));
        when(profileRepository.save(any(YogaProfile.class))).thenReturn(updatedProfile);

        YogaProfileResponse result = yogaProfileService.updateProfile(1L, updateRequest);

        assertThat(result.getWeeklyMinutesAvailable()).isEqualTo(180);
        assertThat(result.getSessionsPerWeek()).isEqualTo(5);
        assertThat(result.getDynamicPreference()).isEqualTo(DynamicPreference.STATIC);
        assertThat(result.getGoals()).containsExactly(goal1);
    }
}
