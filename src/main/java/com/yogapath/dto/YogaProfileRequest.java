package com.yogapath.dto;

import com.yogapath.model.enums.DynamicPreference;
import com.yogapath.model.enums.PhilosophyOpenness;
import com.yogapath.model.enums.StructurePreference;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class YogaProfileRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Weekly minutes is required")
    @Min(value = 15, message = "Weekly minutes must be at least 15")
    @Max(value = 1200, message = "Weekly minutes cannot exceed 1200")
    private Integer weeklyMinutesAvailable;

    @NotNull(message = "Sessions per week is required")
    @Min(value = 1, message = "Sessions per week must be at least 1")
    @Max(value = 7, message = "Sessions per week cannot exceed 7")
    private Integer sessionsPerWeek;

    @NotNull(message = "Dynamic preference is required")
    private DynamicPreference dynamicPreference;

    @NotNull(message = "Structure preference is required")
    private StructurePreference structurePreference;

    @NotNull(message = "Philosophy openness is required")
    private PhilosophyOpenness philosophyOpenness;

    private Set<Long> goalIds;

    public YogaProfileRequest() {}

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

    public Set<Long> getGoalIds() { return goalIds; }
    public void setGoalIds(Set<Long> goalIds) { this.goalIds = goalIds; }
}
