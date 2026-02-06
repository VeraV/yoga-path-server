package com.yogapath.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PracticeLogResponse {

    private Long id;
    private Long userId;
    private LocalDate practiceDate;
    private Integer minutesPracticed;
    private String notes;
    private LocalDateTime createdAt;

    public PracticeLogResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDate getPracticeDate() { return practiceDate; }
    public void setPracticeDate(LocalDate practiceDate) { this.practiceDate = practiceDate; }

    public Integer getMinutesPracticed() { return minutesPracticed; }
    public void setMinutesPracticed(Integer minutesPracticed) { this.minutesPracticed = minutesPracticed; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
