package com.yogapath.service;

import com.yogapath.dto.PracticeLogRequest;
import com.yogapath.dto.PracticeLogResponse;
import com.yogapath.model.PracticeLog;
import com.yogapath.model.User;
import com.yogapath.repository.PracticeLogRepository;
import com.yogapath.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PracticeLogService {

    private final PracticeLogRepository practiceLogRepository;
    private final UserRepository userRepository;

    public PracticeLogService(PracticeLogRepository practiceLogRepository,
                              UserRepository userRepository) {
        this.practiceLogRepository = practiceLogRepository;
        this.userRepository = userRepository;
    }

    public PracticeLogResponse createPracticeLog(PracticeLogRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        PracticeLog log = new PracticeLog();
        log.setUser(user);
        log.setPracticeDate(request.getPracticeDate());
        log.setMinutesPracticed(request.getMinutesPracticed());
        log.setNotes(request.getNotes());

        PracticeLog saved = practiceLogRepository.save(log);
        return toResponse(saved);
    }

    public PracticeLogResponse getPracticeLogById(Long id) {
        PracticeLog log = practiceLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Practice log not found with id: " + id));
        return toResponse(log);
    }

    public List<PracticeLogResponse> getPracticeLogsByUserId(Long userId) {
        List<PracticeLog> logs = practiceLogRepository.findByUserIdOrderByPracticeDateDesc(userId);
        return logs.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<PracticeLogResponse> getPracticeLogsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        List<PracticeLog> logs = practiceLogRepository.findByUserIdAndPracticeDateBetween(userId, startDate, endDate);
        return logs.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PracticeLogResponse updatePracticeLog(Long id, PracticeLogRequest request) {
        PracticeLog log = practiceLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Practice log not found with id: " + id));

        log.setPracticeDate(request.getPracticeDate());
        log.setMinutesPracticed(request.getMinutesPracticed());
        log.setNotes(request.getNotes());

        PracticeLog saved = practiceLogRepository.save(log);
        return toResponse(saved);
    }

    public void deletePracticeLog(Long id) {
        PracticeLog log = practiceLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Practice log not found with id: " + id));
        practiceLogRepository.delete(log);
    }

    private PracticeLogResponse toResponse(PracticeLog log) {
        PracticeLogResponse response = new PracticeLogResponse();
        response.setId(log.getId());
        response.setUserId(log.getUser().getId());
        response.setPracticeDate(log.getPracticeDate());
        response.setMinutesPracticed(log.getMinutesPracticed());
        response.setNotes(log.getNotes());
        response.setCreatedAt(log.getCreatedAt());
        return response;
    }
}
