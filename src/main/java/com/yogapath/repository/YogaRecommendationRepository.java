package com.yogapath.repository;

import com.yogapath.model.YogaRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YogaRecommendationRepository extends JpaRepository<YogaRecommendation, Long> {

    List<YogaRecommendation> findByProfileIdOrderByCreatedAtDesc(Long profileId);

    Optional<YogaRecommendation> findFirstByProfileIdOrderByCreatedAtDesc(Long profileId);
}
