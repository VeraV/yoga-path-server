package com.yogapath.repository;

import com.yogapath.model.YogaStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YogaStyleRepository extends JpaRepository<YogaStyle, Long> {
    List<YogaStyle> findAllByOrderBySortOrderAsc();
}
