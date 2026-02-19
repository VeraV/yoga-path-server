package com.yogapath.repository;

import com.yogapath.model.Limitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LimitationRepository extends JpaRepository<Limitation, Long> {
    List<Limitation> findAllByOrderByNameAsc();
}
