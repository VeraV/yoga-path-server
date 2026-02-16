package com.yogapath.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "yoga_recommendations")
public class YogaRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private YogaProfile profile;

    @Column(nullable = false)
    private Integer asanaMinutes;

    @Column(nullable = false)
    private Integer pranayamaMinutes;

    @Column(nullable = false)
    private Integer meditationMinutes;

    @Column(nullable = false)
    private Integer relaxationMinutes;

    @Column(nullable = false)
    private Integer mantraMinutes;

    @ManyToMany
    @JoinTable(
        name = "recommendation_styles",
        joinColumns = @JoinColumn(name = "recommendation_id"),
        inverseJoinColumns = @JoinColumn(name = "style_id")
    )
    private Set<YogaStyle> styles = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public YogaRecommendation() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public YogaProfile getProfile() { return profile; }
    public void setProfile(YogaProfile profile) { this.profile = profile; }

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

    public Set<YogaStyle> getStyles() { return styles; }
    public void setStyles(Set<YogaStyle> styles) { this.styles = styles; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
