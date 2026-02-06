package com.yogapath.dto;

import com.yogapath.model.Goal;
import com.yogapath.model.enums.DynamicPreference;
import com.yogapath.model.enums.PhilosophyOpenness;
import com.yogapath.model.enums.StructurePreference;

import java.time.LocalDateTime;
import java.util.Set;

public class YogaProfileResponse {

    private Long id;
    private Long userId;
    private Integer weeklyMinutesAvailable;
    private Integer sessionsPerWeek;
    private DynamicPreference dynamicPreference;
    private StructurePreference structurePreference;
    private PhilosophyOpenness philosophyOpenness;
    private Set<Goal> goals;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public YogaProfileResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getWeeklyMinutesAvailable() { return weeklyMinutesAvailable; }
    public void setWeeklyMinutesAvailable(Integer weeklyMinutesAvailable) { this.weeklyMinutesAvailable = weeklyMinutesAvailable; }

    public Integer getSessionsPerWeek() { return sessionsPerWeek; }
    public void setSessionsPerWeek(Integer sessionsPerWeek) { this.sessionsPerWeek = sessionsPerWeek; }

    public DynamicPreference getDynamicPreference() { return dynamicPreference; }
    public void setDynamicPreference(DynamicPreference dynamicPreference) { this.dynamicPreference = dynamicPreference; }

    public StructurePreference getStructurePreference() { return structurePreference; }
    public void setStructurePreference(StructurePreference structurePreference) { this.structurePreference = structurePreference; }

    public PhilosophyOpenness getPhilosophyOpenness() { return philosophyOpenness; }
    public void setPhilosophyOpenness(PhilosophyOpenness philosophyOpenness) { this.philosophyOpenness = philosophyOpenness; }

    public Set<Goal> getGoals() { return goals; }
    public void setGoals(Set<Goal> goals) { this.goals = goals; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
