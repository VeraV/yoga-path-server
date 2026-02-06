package com.yogapath.dto;

import java.time.LocalDate;

public class PracticeLogRequest {

    private Long userId;
    private LocalDate practiceDate;
    private Integer minutesPracticed;
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
