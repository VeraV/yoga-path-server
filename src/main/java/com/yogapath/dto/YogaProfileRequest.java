package com.yogapath.dto;

import com.yogapath.model.enums.DynamicPreference;
import com.yogapath.model.enums.PhilosophyOpenness;
import com.yogapath.model.enums.StructurePreference;

import java.util.Set;

public class YogaProfileRequest {

    private Long userId;
    private Integer weeklyMinutesAvailable;
    private Integer sessionsPerWeek;
    private DynamicPreference dynamicPreference;
    private StructurePreference structurePreference;
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
