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

        allocateMinutes(recommendation, profile, minutesPerSession);

        // Determine yoga styles based on preferences
        List<YogaStyle> styles = determineStyles(profile);
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

        boolean longSession = minutesPerSession >= 30;

        // Base minimums (session-length dependent)
        int asana = longSession ? 15 : 10;
        int pranayama = longSession ? 5 : 3;
        int relaxation = longSession ? 5 : 3;
        int meditation = 0;
        int mantra = 0;

        // Goal adjustments — each +5 applied once regardless of how many goals trigger it
        if (goalNames.contains("Stress Relief") || goalNames.contains("Mental Focus")) {
            pranayama += 5;
        }
        if (goalNames.contains("Stress Relief") || goalNames.contains("Better Sleep")) {
            relaxation += 5;
        }
        if (goalNames.contains("Mental Focus") || goalNames.contains("Interested in Philosophy")) {
            meditation = 5;
        }
        if (goalNames.contains("Interested in Philosophy")) {
            mantra = 5;
        }

        int total = asana + pranayama + relaxation + meditation + mantra;
        int remaining = minutesPerSession - total;

        if (remaining <= 0) {
            // Minimums exceed session time — use as-is, client will show warning
        } else if (remaining <= 5) {
            // Small remainder goes entirely to asana
            asana += remaining;
        } else {
            // Scale all components proportionally to fill the session
            double scale = (double) minutesPerSession / total;
            asana = (int) (asana * scale);
            pranayama = (int) (pranayama * scale);
            relaxation = (int) (relaxation * scale);
            meditation = (int) (meditation * scale);
            mantra = (int) (mantra * scale);
            // Add rounding remainder to asana to ensure exact total
            int scaledTotal = asana + pranayama + relaxation + meditation + mantra;
            asana += minutesPerSession - scaledTotal;
        }

        rec.setAsanaMinutes(asana);
        rec.setPranayamaMinutes(pranayama);
        rec.setRelaxationMinutes(relaxation);
        rec.setMeditationMinutes(meditation);
        rec.setMantraMinutes(mantra);
    }

    private List<YogaStyle> determineStyles(YogaProfile profile) {
        List<YogaStyle> allStyles = styleRepository.findAllByOrderBySortOrderAsc();

        DynamicPreference dynamic = profile.getDynamicPreference();
        StructurePreference structure = profile.getStructurePreference();
        PhilosophyOpenness philosophy = profile.getPhilosophyOpenness();

        return allStyles.stream()
                .filter(style -> structure != StructurePreference.STRUCTURED || style.isStructured())
                .filter(style -> structure != StructurePreference.CREATIVE || style.isCreative())
                .filter(style -> dynamic != DynamicPreference.DYNAMIC || style.isDynamic())
                .filter(style -> dynamic != DynamicPreference.STATIC || style.isStatik())
                .filter(style -> philosophy != PhilosophyOpenness.NOT_OPEN || !style.isRequiresPhilosophyOpenness())
                .collect(Collectors.toList());
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
