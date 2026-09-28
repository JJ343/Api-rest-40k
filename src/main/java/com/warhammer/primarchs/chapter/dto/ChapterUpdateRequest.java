package com.warhammer.primarchs.chapter.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record ChapterUpdateRequest(
        @Size(max = 100) String name,
        @Min(1) Integer founding,
        UUID primarchId,
        @Size(max = 100) String homeworld,
        @Size(max = 120) String fortressMonastery,
        Boolean codexCompliant,
        @Min(0) Integer approxStrength,
        String description) {
}
