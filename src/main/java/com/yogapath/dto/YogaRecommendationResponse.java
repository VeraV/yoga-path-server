package com.yogapath.dto;

import com.yogapath.model.YogaStyle;

import java.time.LocalDateTime;
import java.util.Set;

public class YogaRecommendationResponse {

    private Long id;
    private Long profileId;
    private Integer asanaMinutes;
    private Integer pranayamaMinutes;
    private Integer meditationMinutes;
    private Integer relaxationMinutes;
    private Integer mantraMinutes;
    private Integer totalMinutesPerSession;
    private Set<YogaStyle> styles;
    private Boolean isOutdated;
    private LocalDateTime createdAt;

    public YogaRecommendationResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProfileId() { return profileId; }
    public void setProfileId(Long profileId) { this.profileId = profileId; }

    public Integer getAsanaMinutes() { return asanaMinutes; }
    public void setAsanaMinutes(Integer asanaMinutes) { this.asanaMinutes = asanaMinutes; }

    public Integer getPranayamaMinutes() { return pranayamaMinutes; }
    public void setPranayamaMinutes(Integer pranayamaMinutes) { this.pranayamaMinutes = pranayamaMinutes; }

    public Integer getMeditationMinutes() { return meditationMinutes; }
    public void setMeditationMinutes(Integer meditationMinutes) { this.meditationMinutes = meditationMinutes; }

    public Integer getRelaxationMinutes() { return relaxationMinutes; }
    public void setRelaxationMinutes(Integer relaxationMinutes) { this.relaxationMinutes = relaxationMinutes; }

    public Integer getMantraMinutes() { return mantraMinutes; }
    public void setMantraMinutes(Integer mantraMinutes) { this.mantraMinutes = mantraMinutes; }

    public Integer getTotalMinutesPerSession() { return totalMinutesPerSession; }
    public void setTotalMinutesPerSession(Integer totalMinutesPerSession) { this.totalMinutesPerSession = totalMinutesPerSession; }

    public Set<YogaStyle> getStyles() { return styles; }
    public void setStyles(Set<YogaStyle> styles) { this.styles = styles; }

    public Boolean getIsOutdated() { return isOutdated; }
    public void setIsOutdated(Boolean isOutdated) { this.isOutdated = isOutdated; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
