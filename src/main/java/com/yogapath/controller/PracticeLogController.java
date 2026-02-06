package com.yogapath.controller;

import com.yogapath.dto.PracticeLogRequest;
import com.yogapath.dto.PracticeLogResponse;
import com.yogapath.service.PracticeLogService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/practice-logs")
public class PracticeLogController {

    private final PracticeLogService practiceLogService;

    public PracticeLogController(PracticeLogService practiceLogService) {
        this.practiceLogService = practiceLogService;
    }

    @PostMapping
    public ResponseEntity<PracticeLogResponse> createPracticeLog(@Valid @RequestBody PracticeLogRequest request) {
        PracticeLogResponse response = practiceLogService.createPracticeLog(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public PracticeLogResponse getPracticeLogById(@PathVariable Long id) {
        return practiceLogService.getPracticeLogById(id);
    }

    @GetMapping("/user/{userId}")
    public List<PracticeLogResponse> getPracticeLogsByUserId(@PathVariable Long userId) {
        return practiceLogService.getPracticeLogsByUserId(userId);
    }

    @GetMapping("/user/{userId}/range")
    public List<PracticeLogResponse> getPracticeLogsByDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return practiceLogService.getPracticeLogsByDateRange(userId, startDate, endDate);
    }

    @PutMapping("/{id}")
    public PracticeLogResponse updatePracticeLog(@PathVariable Long id, @Valid @RequestBody PracticeLogRequest request) {
        return practiceLogService.updatePracticeLog(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePracticeLog(@PathVariable Long id) {
        practiceLogService.deletePracticeLog(id);
        return ResponseEntity.noContent().build();
    }
}
