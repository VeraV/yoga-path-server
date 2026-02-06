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
}
