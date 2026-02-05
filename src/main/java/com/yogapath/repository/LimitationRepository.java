package com.yogapath.repository;

import com.yogapath.model.Limitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LimitationRepository extends JpaRepository<Limitation, Long> {
}
