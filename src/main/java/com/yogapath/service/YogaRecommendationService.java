package com.yogapath.service;

import com.yogapath.dto.YogaRecommendationResponse;
import com.yogapath.model.Goal;
import com.yogapath.model.YogaProfile;
import com.yogapath.model.YogaRecommendation;
import com.yogapath.model.YogaStyle;
import com.yogapath.model.enums.DynamicPreference;
import com.yogapath.model.enums.PhilosophyOpenness;
import com.yogapath.model.enums.StructurePreference;
import com.yogapath.repository.YogaProfileRepository;
import com.yogapath.repository.YogaRecommendationRepository;
import com.yogapath.repository.YogaStyleRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class YogaRecommendationService {

    private final YogaRecommendationRepository recommendationRepository;
    private final YogaProfileRepository profileRepository;
    private final YogaStyleRepository styleRepository;

    public YogaRecommendationService(YogaRecommendationRepository recommendationRepository,
                                     YogaProfileRepository profileRepository,
                                     YogaStyleRepository styleRepository) {
        this.recommendationRepository = recommendationRepository;
        this.profileRepository = profileRepository;
        this.styleRepository = styleRepository;
    }

    public YogaRecommendationResponse generateRecommendation(Long profileId) {
        YogaProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        YogaRecommendation recommendation = new YogaRecommendation();
        recommendation.setProfile(profile);

        // Calculate minutes per session
        int minutesPerSession = profile.getWeeklyMinutesAvailable() / profile.getSessionsPerWeek();

        // Allocate time based on goals
        allocateMinutes(recommendation, profile, minutesPerSession);

        // Determine yoga styles based on preferences
        Set<YogaStyle> styles = determineStyles(profile);
        recommendation.setStyles(styles);

        YogaRecommendation saved = recommendationRepository.save(recommendation);
        return toResponse(saved, profile, false);
    }

    public YogaRecommendationResponse getLatestRecommendation(Long profileId) {
        YogaProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        YogaRecommendation recommendation = recommendationRepository
                .findFirstByProfileIdOrderByCreatedAtDesc(profileId)
                .orElseThrow(() -> new RuntimeException("No recommendations found for profile: " + profileId));

        boolean isOutdated = profile.getUpdatedAt().isAfter(recommendation.getCreatedAt());
        return toResponse(recommendation, profile, isOutdated);
    }

    public List<YogaRecommendationResponse> getRecommendationHistory(Long profileId) {
        YogaProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        List<YogaRecommendation> recommendations = recommendationRepository
                .findByProfileIdOrderByCreatedAtDesc(profileId);

        return recommendations.stream()
                .map(rec -> {
                    boolean isOutdated = profile.getUpdatedAt().isAfter(rec.getCreatedAt());
                    return toResponse(rec, profile, isOutdated);
                })
                .collect(Collectors.toList());
    }

    private void allocateMinutes(YogaRecommendation rec, YogaProfile profile, int minutesPerSession) {
        Set<String> goalNames = profile.getGoals().stream()
                .map(Goal::getName)
                .collect(Collectors.toSet());

        // Base allocation (minimum for any practice)
        int asana = 10;
        int pranayama = 5;
        int meditation = 5;
        int relaxation = 5;
        int mantra = 0;

        // Adjust based on goals
        if (goalNames.contains("Physical Fitness") || goalNames.contains("Flexibility")) {
            asana += 15;
        }
        if (goalNames.contains("Stress Relief") || goalNames.contains("Better Sleep")) {
            relaxation += 10;
            pranayama += 5;
        }
        if (goalNames.contains("Mental Focus")) {
            meditation += 10;
            pranayama += 5;
        }
        if (goalNames.contains("Interested in Philosophy")) {
            mantra += 10;
            meditation += 5;
        }

        // Scale to fit available time
        int totalRequested = asana + pranayama + meditation + relaxation + mantra;
        if (totalRequested > minutesPerSession) {
            double scale = (double) minutesPerSession / totalRequested;
            asana = (int) (asana * scale);
            pranayama = (int) (pranayama * scale);
            meditation = (int) (meditation * scale);
            relaxation = (int) (relaxation * scale);
            mantra = (int) (mantra * scale);
        }

        rec.setAsanaMinutes(asana);
        rec.setPranayamaMinutes(pranayama);
        rec.setMeditationMinutes(meditation);
        rec.setRelaxationMinutes(relaxation);
        rec.setMantraMinutes(mantra);
    }

    private Set<YogaStyle> determineStyles(YogaProfile profile) {
        List<YogaStyle> allStyles = styleRepository.findAll();
        Set<YogaStyle> recommended = new HashSet<>();

        DynamicPreference dynamic = profile.getDynamicPreference();
        StructurePreference structure = profile.getStructurePreference();
        PhilosophyOpenness philosophy = profile.getPhilosophyOpenness();

        for (YogaStyle style : allStyles) {
            String name = style.getName();

            // DYNAMIC + STRUCTURED -> Ashtanga
            if (name.equals("Ashtanga")) {
                if ((dynamic == DynamicPreference.DYNAMIC || dynamic == DynamicPreference.NO_PREFERENCE) &&
                    (structure == StructurePreference.STRUCTURED || structure == StructurePreference.NO_PREFERENCE)) {
                    recommended.add(style);
                }
            }

            // STATIC + STRUCTURED -> Sivananda, Iyengar
            if (name.equals("Sivananda") || name.equals("Iyengar")) {
                if ((dynamic == DynamicPreference.STATIC || dynamic == DynamicPreference.NO_PREFERENCE) &&
                    (structure == StructurePreference.STRUCTURED || structure == StructurePreference.NO_PREFERENCE)) {
                    recommended.add(style);
                }
            }

            // STATIC + STRUCTURED + OPEN -> Kundalini
            if (name.equals("Kundalini")) {
                if ((dynamic == DynamicPreference.STATIC || dynamic == DynamicPreference.NO_PREFERENCE) &&
                    (structure == StructurePreference.STRUCTURED || structure == StructurePreference.NO_PREFERENCE) &&
                    (philosophy == PhilosophyOpenness.OPEN || philosophy == PhilosophyOpenness.NO_PREFERENCE)) {
                    recommended.add(style);
                }
            }

            // DYNAMIC + CREATIVE -> Vinyasa
            if (name.equals("Vinyasa")) {
                if ((dynamic == DynamicPreference.DYNAMIC || dynamic == DynamicPreference.NO_PREFERENCE) &&
                    (structure == StructurePreference.CREATIVE || structure == StructurePreference.NO_PREFERENCE)) {
                    recommended.add(style);
                }
            }

            // STATIC + CREATIVE -> Yin, Hatha
            if (name.equals("Yin") || name.equals("Hatha")) {
                if ((dynamic == DynamicPreference.STATIC || dynamic == DynamicPreference.NO_PREFERENCE) &&
                    (structure == StructurePreference.CREATIVE || structure == StructurePreference.NO_PREFERENCE)) {
                    recommended.add(style);
                }
            }
        }

        return recommended;
    }

    private YogaRecommendationResponse toResponse(YogaRecommendation rec, YogaProfile profile, boolean isOutdated) {
        YogaRecommendationResponse response = new YogaRecommendationResponse();
        response.setId(rec.getId());
        response.setProfileId(profile.getId());
        response.setAsanaMinutes(rec.getAsanaMinutes());
        response.setPranayamaMinutes(rec.getPranayamaMinutes());
        response.setMeditationMinutes(rec.getMeditationMinutes());
        response.setRelaxationMinutes(rec.getRelaxationMinutes());
        response.setMantraMinutes(rec.getMantraMinutes());

        int total = rec.getAsanaMinutes() + rec.getPranayamaMinutes() +
                    rec.getMeditationMinutes() + rec.getRelaxationMinutes() + rec.getMantraMinutes();
        response.setTotalMinutesPerSession(total);

        response.setStyles(rec.getStyles());
        response.setIsOutdated(isOutdated);
        response.setCreatedAt(rec.getCreatedAt());
        return response;
    }
}
