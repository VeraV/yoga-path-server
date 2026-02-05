package com.yogapath.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "yoga_recommendations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Builder.Default
    private Set<YogaStyle> styles = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
