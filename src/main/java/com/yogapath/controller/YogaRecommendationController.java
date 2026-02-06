package com.yogapath.controller;

import com.yogapath.dto.YogaRecommendationResponse;
import com.yogapath.service.YogaRecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class YogaRecommendationController {

    private final YogaRecommendationService recommendationService;

    public YogaRecommendationController(YogaRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/generate/{profileId}")
    public ResponseEntity<YogaRecommendationResponse> generateRecommendation(@PathVariable Long profileId) {
        YogaRecommendationResponse response = recommendationService.generateRecommendation(profileId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/profile/{profileId}/latest")
    public YogaRecommendationResponse getLatestRecommendation(@PathVariable Long profileId) {
        return recommendationService.getLatestRecommendation(profileId);
    }

    @GetMapping("/profile/{profileId}")
    public List<YogaRecommendationResponse> getRecommendationHistory(@PathVariable Long profileId) {
        return recommendationService.getRecommendationHistory(profileId);
    }
}
