package com.warhammer.primarchs.chapter;

import com.warhammer.primarchs.primarch.Primarch;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "chapters")
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer founding;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "primarch_id", nullable = false)
    private Primarch primarch;

    @Column(length = 100)
    private String homeworld;

    @Column(name = "fortress_monastery", length = 120)
    private String fortressMonastery;

    @Column(name = "codex_compliant", nullable = false)
    private boolean codexCompliant = true;

    @Column(name = "approx_strength")
    private Integer approxStrength;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Chapter() {
    }

    public Chapter(String name, Integer founding, Primarch primarch, String homeworld,
                   String fortressMonastery, boolean codexCompliant,
                   Integer approxStrength, String description) {
        this.name = name;
        this.founding = founding;
        this.primarch = primarch;
        this.homeworld = homeworld;
        this.fortressMonastery = fortressMonastery;
        this.codexCompliant = codexCompliant;
        this.approxStrength = approxStrength;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFounding() {
        return founding;
    }

    public void setFounding(Integer founding) {
        this.founding = founding;
    }

    public Primarch getPrimarch() {
        return primarch;
    }

    public void setPrimarch(Primarch primarch) {
        this.primarch = primarch;
    }

    public String getHomeworld() {
        return homeworld;
    }

    public void setHomeworld(String homeworld) {
        this.homeworld = homeworld;
    }

    public String getFortressMonastery() {
        return fortressMonastery;
    }

    public void setFortressMonastery(String fortressMonastery) {
        this.fortressMonastery = fortressMonastery;
    }

    public boolean isCodexCompliant() {
        return codexCompliant;
    }

    public void setCodexCompliant(boolean codexCompliant) {
        this.codexCompliant = codexCompliant;
    }

    public Integer getApproxStrength() {
        return approxStrength;
    }

    public void setApproxStrength(Integer approxStrength) {
        this.approxStrength = approxStrength;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
