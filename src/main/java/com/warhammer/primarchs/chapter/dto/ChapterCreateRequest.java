package com.warhammer.primarchs.chapter.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record ChapterCreateRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull @Min(1) Integer founding,
        @NotNull UUID primarchId,
        @Size(max = 100) String homeworld,
        @Size(max = 120) String fortressMonastery,
        boolean codexCompliant,
        @Min(0) Integer approxStrength,
        String description) {
}
