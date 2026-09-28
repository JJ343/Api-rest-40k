package com.warhammer.primarchs.primarch.dto;

import com.warhammer.primarchs.primarch.Allegiance;
import com.warhammer.primarchs.primarch.PrimarchStatus;
import java.time.Instant;
import java.util.UUID;

public record PrimarchResponse(
        UUID id,
        String name,
        Integer legionNumber,
        Allegiance allegiance,
        PrimarchStatus status,
        String homeworld,
        String description,
        Instant createdAt,
        Instant updatedAt) {
}
