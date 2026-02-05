package com.yogapath.repository;

import com.yogapath.model.PracticeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PracticeLogRepository extends JpaRepository<PracticeLog, Long> {

    List<PracticeLog> findByUserIdOrderByPracticeDateDesc(Long userId);

    List<PracticeLog> findByUserIdAndPracticeDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
