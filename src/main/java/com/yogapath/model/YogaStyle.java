package com.yogapath.model;

import jakarta.persistence.*;

@Entity
@Table(name = "yoga_styles")
public class YogaStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Style characteristics for matching with user preferences
    @Column(name = "is_structured")
    private boolean structured;

    @Column(name = "is_creative")
    private boolean creative;

    @Column(name = "is_dynamic")
    private boolean dynamic;

    @Column(name = "is_static")
    private boolean statik;  // "static" is a reserved word in Java

    @Column(name = "requires_philosophy_openness")
    private boolean requiresPhilosophyOpenness;

    @Column(name = "sort_order")
    private int sortOrder;

    public YogaStyle() {}

    public YogaStyle(Long id, String name, String description, String notes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.notes = notes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isStructured() { return structured; }
    public void setStructured(boolean structured) { this.structured = structured; }

    public boolean isCreative() { return creative; }
    public void setCreative(boolean creative) { this.creative = creative; }

    public boolean isDynamic() { return dynamic; }
    public void setDynamic(boolean dynamic) { this.dynamic = dynamic; }

    public boolean isStatik() { return statik; }
    public void setStatik(boolean statik) { this.statik = statik; }

    public boolean isRequiresPhilosophyOpenness() { return requiresPhilosophyOpenness; }
    public void setRequiresPhilosophyOpenness(boolean requiresPhilosophyOpenness) {
        this.requiresPhilosophyOpenness = requiresPhilosophyOpenness;
    }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
