package com.yogapath.repository;

import com.yogapath.model.YogaProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface YogaProfileRepository extends JpaRepository<YogaProfile, Long> {

    Optional<YogaProfile> findByUserId(Long userId);
}
