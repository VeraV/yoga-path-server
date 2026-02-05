package com.yogapath.repository;

import com.yogapath.model.YogaStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YogaStyleRepository extends JpaRepository<YogaStyle, Long> {
}
