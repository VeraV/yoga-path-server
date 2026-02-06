package com.yogapath.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class PracticeLogRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Practice date is required")
    private LocalDate practiceDate;

    @NotNull(message = "Minutes practiced is required")
    @Min(value = 1, message = "Minutes practiced must be at least 1")
    @Max(value = 480, message = "Minutes practiced cannot exceed 480")
    private Integer minutesPracticed;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

    public PracticeLogRequest() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDate getPracticeDate() { return practiceDate; }
    public void setPracticeDate(LocalDate practiceDate) { this.practiceDate = practiceDate; }

    public Integer getMinutesPracticed() { return minutesPracticed; }
    public void setMinutesPracticed(Integer minutesPracticed) { this.minutesPracticed = minutesPracticed; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
