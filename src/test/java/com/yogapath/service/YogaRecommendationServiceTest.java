package com.yogapath.service;

import com.yogapath.dto.YogaRecommendationResponse;
import com.yogapath.model.Goal;
import com.yogapath.model.User;
import com.yogapath.model.YogaProfile;
import com.yogapath.model.YogaRecommendation;
import com.yogapath.model.YogaStyle;
import com.yogapath.model.enums.DynamicPreference;
import com.yogapath.model.enums.PhilosophyOpenness;
import com.yogapath.model.enums.StructurePreference;
import com.yogapath.repository.YogaProfileRepository;
import com.yogapath.repository.YogaRecommendationRepository;
import com.yogapath.repository.YogaStyleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class YogaRecommendationServiceTest {

    @Mock
    private YogaRecommendationRepository recommendationRepository;

    @Mock
    private YogaProfileRepository profileRepository;

    @Mock
    private YogaStyleRepository styleRepository;

    @InjectMocks
    private YogaRecommendationService yogaRecommendationService;

    private User user;
    private YogaProfile profile;
    private YogaRecommendation savedRecommendation;
    private YogaStyle structuredDynamicStyle;
    private YogaStyle creativeStaticStyle;
    private YogaStyle philosophyStyle;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Vera", "vera@example.com", "hashed", true);

        profile = new YogaProfile();
        profile.setId(1L);
        profile.setUser(user);
        profile.setWeeklyMinutesAvailable(120);
        profile.setSessionsPerWeek(3);  // minutesPerSession = 40
        profile.setDynamicPreference(DynamicPreference.NO_PREFERENCE);
        profile.setStructurePreference(StructurePreference.NO_PREFERENCE);
        profile.setPhilosophyOpenness(PhilosophyOpenness.NO_PREFERENCE);
        profile.setGoals(new HashSet<>());
        profile.setUpdatedAt(LocalDateTime.of(2026, 3, 1, 10, 0));

        structuredDynamicStyle = new YogaStyle();
        structuredDynamicStyle.setId(1L);
        structuredDynamicStyle.setName("Ashtanga");
        structuredDynamicStyle.setStructured(true);
        structuredDynamicStyle.setDynamic(true);
        structuredDynamicStyle.setCreative(false);
        structuredDynamicStyle.setStatik(false);
        structuredDynamicStyle.setRequiresPhilosophyOpenness(false);

        creativeStaticStyle = new YogaStyle();
        creativeStaticStyle.setId(2L);
        creativeStaticStyle.setName("Yin");
        creativeStaticStyle.setStructured(false);
        creativeStaticStyle.setDynamic(false);
        creativeStaticStyle.setCreative(true);
        creativeStaticStyle.setStatik(true);
        creativeStaticStyle.setRequiresPhilosophyOpenness(false);

        philosophyStyle = new YogaStyle();
        philosophyStyle.setId(3L);
        philosophyStyle.setName("Kundalini");
        philosophyStyle.setStructured(false);
        philosophyStyle.setDynamic(false);
        philosophyStyle.setCreative(true);
        philosophyStyle.setStatik(false);
        philosophyStyle.setRequiresPhilosophyOpenness(true);

        savedRecommendation = new YogaRecommendation();
        savedRecommendation.setId(1L);
        savedRecommendation.setProfile(profile);
        savedRecommendation.setAsanaMinutes(24);
        savedRecommendation.setPranayamaMinutes(8);
        savedRecommendation.setRelaxationMinutes(8);
        savedRecommendation.setMeditationMinutes(0);
        savedRecommendation.setMantraMinutes(0);
        savedRecommendation.setStyles(List.of(structuredDynamicStyle, creativeStaticStyle));
        savedRecommendation.setCreatedAt(LocalDateTime.of(2026, 3, 5, 10, 0));
    }

    // --- generateRecommendation ---

    @Test
    void generateRecommendation_throwsException_whenProfileNotFound() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> yogaRecommendationService.generateRecommendation(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Profile not found with id: 99");

        verify(recommendationRepository, never()).save(any());
    }

    @Test
    void generateRecommendation_returnsResponse_whenProfileExists() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(styleRepository.findAllByOrderBySortOrderAsc())
                .thenReturn(List.of(structuredDynamicStyle, creativeStaticStyle));
        when(recommendationRepository.save(any(YogaRecommendation.class))).thenReturn(savedRecommendation);

        YogaRecommendationResponse result = yogaRecommendationService.generateRecommendation(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getProfileId()).isEqualTo(1L);
        assertThat(result.getIsOutdated()).isFalse();
    }

    @Test
    void generateRecommendation_allocatesMinutesCorrectly_forNoGoals() {
        // minutesPerSession = 120/3 = 40
        // preferred minimums: asana=15, pranayama=5, relaxation=5, meditation=0, mantra=0 → total=25
        // remaining=15 > 5 → scale = 40.0/25 = 1.6
        // asana=(int)(15*1.6)=24, pranayama=(int)(5*1.6)=8, relaxation=(int)(5*1.6)=8
        // total=40, rounding remainder=0

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(styleRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of());
        when(recommendationRepository.save(any(YogaRecommendation.class))).thenReturn(savedRecommendation);

        yogaRecommendationService.generateRecommendation(1L);

        ArgumentCaptor<YogaRecommendation> captor = ArgumentCaptor.forClass(YogaRecommendation.class);
        verify(recommendationRepository).save(captor.capture());

        YogaRecommendation captured = captor.getValue();
        assertThat(captured.getAsanaMinutes()).isEqualTo(24);
        assertThat(captured.getPranayamaMinutes()).isEqualTo(8);
        assertThat(captured.getRelaxationMinutes()).isEqualTo(8);
        assertThat(captured.getMeditationMinutes()).isEqualTo(0);
        assertThat(captured.getMantraMinutes()).isEqualTo(0);
    }

    @Test
    void generateRecommendation_totalMinutesPerSessionMatchesCalculation() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(styleRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of());
        when(recommendationRepository.save(any(YogaRecommendation.class))).thenReturn(savedRecommendation);

        YogaRecommendationResponse result = yogaRecommendationService.generateRecommendation(1L);

        assertThat(result.getTotalMinutesPerSession()).isEqualTo(40);
    }

    @Test
    void generateRecommendation_addsMeditationMinutes_whenMentalFocusGoal() {
        Goal mentalFocus = new Goal(1L, "Mental Focus", "Improve concentration", null);
        profile.setGoals(Set.of(mentalFocus));

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(styleRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of());
        when(recommendationRepository.save(any(YogaRecommendation.class))).thenReturn(savedRecommendation);

        yogaRecommendationService.generateRecommendation(1L);

        ArgumentCaptor<YogaRecommendation> captor = ArgumentCaptor.forClass(YogaRecommendation.class);
        verify(recommendationRepository).save(captor.capture());

        assertThat(captor.getValue().getMeditationMinutes()).isGreaterThan(0);
    }

    @Test
    void generateRecommendation_addsMantraMinutes_whenPhilosophyGoal() {
        Goal philosophy = new Goal(2L, "Interested in Philosophy", "Explore yoga philosophy", null);
        profile.setGoals(Set.of(philosophy));

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(styleRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of());
        when(recommendationRepository.save(any(YogaRecommendation.class))).thenReturn(savedRecommendation);

        yogaRecommendationService.generateRecommendation(1L);

        ArgumentCaptor<YogaRecommendation> captor = ArgumentCaptor.forClass(YogaRecommendation.class);
        verify(recommendationRepository).save(captor.capture());

        assertThat(captor.getValue().getMantraMinutes()).isGreaterThan(0);
        assertThat(captor.getValue().getMeditationMinutes()).isGreaterThan(0);
    }

    @Test
    void generateRecommendation_filtersStylesByStructuredPreference() {
        profile.setStructurePreference(StructurePreference.STRUCTURED);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(styleRepository.findAllByOrderBySortOrderAsc())
                .thenReturn(List.of(structuredDynamicStyle, creativeStaticStyle));
        when(recommendationRepository.save(any(YogaRecommendation.class))).thenReturn(savedRecommendation);

        yogaRecommendationService.generateRecommendation(1L);

        ArgumentCaptor<YogaRecommendation> captor = ArgumentCaptor.forClass(YogaRecommendation.class);
        verify(recommendationRepository).save(captor.capture());

        List<YogaStyle> styles = captor.getValue().getStyles();
        assertThat(styles).contains(structuredDynamicStyle);
        assertThat(styles).doesNotContain(creativeStaticStyle);
    }

    @Test
    void generateRecommendation_filtersStylesByDynamicPreference() {
        profile.setDynamicPreference(DynamicPreference.DYNAMIC);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(styleRepository.findAllByOrderBySortOrderAsc())
                .thenReturn(List.of(structuredDynamicStyle, creativeStaticStyle));
        when(recommendationRepository.save(any(YogaRecommendation.class))).thenReturn(savedRecommendation);

        yogaRecommendationService.generateRecommendation(1L);

        ArgumentCaptor<YogaRecommendation> captor = ArgumentCaptor.forClass(YogaRecommendation.class);
        verify(recommendationRepository).save(captor.capture());

        List<YogaStyle> styles = captor.getValue().getStyles();
        assertThat(styles).contains(structuredDynamicStyle);
        assertThat(styles).doesNotContain(creativeStaticStyle);
    }

    @Test
    void generateRecommendation_excludesPhilosophyStyles_whenNotOpenToPhilosophy() {
        profile.setPhilosophyOpenness(PhilosophyOpenness.NOT_OPEN);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(styleRepository.findAllByOrderBySortOrderAsc())
                .thenReturn(List.of(structuredDynamicStyle, creativeStaticStyle, philosophyStyle));
        when(recommendationRepository.save(any(YogaRecommendation.class))).thenReturn(savedRecommendation);

        yogaRecommendationService.generateRecommendation(1L);

        ArgumentCaptor<YogaRecommendation> captor = ArgumentCaptor.forClass(YogaRecommendation.class);
        verify(recommendationRepository).save(captor.capture());

        assertThat(captor.getValue().getStyles()).doesNotContain(philosophyStyle);
    }

    @Test
    void generateRecommendation_includesAllStyles_whenNoPreferences() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(styleRepository.findAllByOrderBySortOrderAsc())
                .thenReturn(List.of(structuredDynamicStyle, creativeStaticStyle, philosophyStyle));
        when(recommendationRepository.save(any(YogaRecommendation.class))).thenReturn(savedRecommendation);

        yogaRecommendationService.generateRecommendation(1L);

        ArgumentCaptor<YogaRecommendation> captor = ArgumentCaptor.forClass(YogaRecommendation.class);
        verify(recommendationRepository).save(captor.capture());

        assertThat(captor.getValue().getStyles())
                .containsExactlyInAnyOrder(structuredDynamicStyle, creativeStaticStyle, philosophyStyle);
    }

    // --- getLatestRecommendation ---

    @Test
    void getLatestRecommendation_throwsException_whenProfileNotFound() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> yogaRecommendationService.getLatestRecommendation(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Profile not found with id: 99");
    }

    @Test
    void getLatestRecommendation_throwsException_whenNoRecommendationExists() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(recommendationRepository.findFirstByProfileIdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> yogaRecommendationService.getLatestRecommendation(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No recommendations found for profile: 1");
    }

    @Test
    void getLatestRecommendation_returnsIsOutdatedFalse_whenProfileNotUpdatedAfterRecommendation() {
        // profile.updatedAt (March 1) is BEFORE recommendation.createdAt (March 5)
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(recommendationRepository.findFirstByProfileIdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.of(savedRecommendation));

        YogaRecommendationResponse result = yogaRecommendationService.getLatestRecommendation(1L);

        assertThat(result.getIsOutdated()).isFalse();
    }

    @Test
    void getLatestRecommendation_returnsIsOutdatedTrue_whenProfileUpdatedAfterRecommendation() {
        // profile.updatedAt (March 10) is AFTER recommendation.createdAt (March 5)
        profile.setUpdatedAt(LocalDateTime.of(2026, 3, 10, 10, 0));

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(recommendationRepository.findFirstByProfileIdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.of(savedRecommendation));

        YogaRecommendationResponse result = yogaRecommendationService.getLatestRecommendation(1L);

        assertThat(result.getIsOutdated()).isTrue();
    }

    // --- getRecommendationHistory ---

    @Test
    void getRecommendationHistory_throwsException_whenProfileNotFound() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> yogaRecommendationService.getRecommendationHistory(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Profile not found with id: 99");
    }

    @Test
    void getRecommendationHistory_returnsEmptyList_whenNoRecommendations() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(recommendationRepository.findByProfileIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of());

        List<YogaRecommendationResponse> result = yogaRecommendationService.getRecommendationHistory(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void getRecommendationHistory_setsCorrectIsOutdatedPerRecommendation() {
        // profile.updatedAt = March 1
        // rec1.createdAt = March 5 (after update → NOT outdated)
        // rec2.createdAt = Feb 20 (before update → outdated)
        YogaRecommendation rec2 = new YogaRecommendation();
        rec2.setId(2L);
        rec2.setProfile(profile);
        rec2.setAsanaMinutes(20);
        rec2.setPranayamaMinutes(5);
        rec2.setRelaxationMinutes(5);
        rec2.setMeditationMinutes(0);
        rec2.setMantraMinutes(0);
        rec2.setStyles(List.of());
        rec2.setCreatedAt(LocalDateTime.of(2026, 2, 20, 10, 0));

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(recommendationRepository.findByProfileIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(savedRecommendation, rec2));

        List<YogaRecommendationResponse> result = yogaRecommendationService.getRecommendationHistory(1L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getIsOutdated()).isFalse();  // rec1 created after profile update
        assertThat(result.get(1).getIsOutdated()).isTrue();   // rec2 created before profile update
    }
}
