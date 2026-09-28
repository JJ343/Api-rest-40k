package com.warhammer.primarchs.primarch.dto;

import com.warhammer.primarchs.primarch.Allegiance;
import com.warhammer.primarchs.primarch.PrimarchStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record PrimarchUpdateRequest(
        @Size(max = 100) String name,
        @Min(1) @Max(20) Integer legionNumber,
        Allegiance allegiance,
        PrimarchStatus status,
        @Size(max = 100) String homeworld,
        String description) {
}
