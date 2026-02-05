package com.yogapath.model;

import com.yogapath.model.enums.DynamicPreference;
import com.yogapath.model.enums.PhilosophyOpenness;
import com.yogapath.model.enums.StructurePreference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "yoga_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YogaProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private Integer weeklyMinutesAvailable;

    @Column(nullable = false)
    private Integer sessionsPerWeek;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DynamicPreference dynamicPreference = DynamicPreference.NO_PREFERENCE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StructurePreference structurePreference = StructurePreference.NO_PREFERENCE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PhilosophyOpenness philosophyOpenness = PhilosophyOpenness.NO_PREFERENCE;

    @ManyToMany
    @JoinTable(
        name = "profile_goals",
        joinColumns = @JoinColumn(name = "profile_id"),
        inverseJoinColumns = @JoinColumn(name = "goal_id")
    )
    @Builder.Default
    private Set<Goal> goals = new HashSet<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<YogaRecommendation> recommendations = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
