package com.warhammer.primarchs.primarch;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "primarchs")
public class Primarch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "legion_number", nullable = false, unique = true)
    private Integer legionNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Allegiance allegiance = Allegiance.UNKNOWN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PrimarchStatus status = PrimarchStatus.UNKNOWN;

    @Column(length = 100)
    private String homeworld;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Primarch() {
    }

    public Primarch(String name, Integer legionNumber, Allegiance allegiance,
                    PrimarchStatus status, String homeworld, String description) {
        this.name = name;
        this.legionNumber = legionNumber;
        this.allegiance = allegiance;
        this.status = status;
        this.homeworld = homeworld;
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

    public Integer getLegionNumber() {
        return legionNumber;
    }

    public void setLegionNumber(Integer legionNumber) {
        this.legionNumber = legionNumber;
    }

    public Allegiance getAllegiance() {
        return allegiance;
    }

    public void setAllegiance(Allegiance allegiance) {
        this.allegiance = allegiance;
    }

    public PrimarchStatus getStatus() {
        return status;
    }

    public void setStatus(PrimarchStatus status) {
        this.status = status;
    }

    public String getHomeworld() {
        return homeworld;
    }

    public void setHomeworld(String homeworld) {
        this.homeworld = homeworld;
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
