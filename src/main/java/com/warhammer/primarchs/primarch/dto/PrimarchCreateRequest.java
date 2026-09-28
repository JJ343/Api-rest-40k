package com.warhammer.primarchs.primarch.dto;

import com.warhammer.primarchs.primarch.Allegiance;
import com.warhammer.primarchs.primarch.PrimarchStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PrimarchCreateRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull @Min(1) @Max(20) Integer legionNumber,
        @NotNull Allegiance allegiance,
        @NotNull PrimarchStatus status,
        @Size(max = 100) String homeworld,
        String description) {
}
