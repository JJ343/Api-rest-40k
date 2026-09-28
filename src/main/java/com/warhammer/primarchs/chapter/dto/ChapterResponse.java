package com.warhammer.primarchs.chapter.dto;

import java.time.Instant;
import java.util.UUID;

public record ChapterResponse(
        UUID id,
        String name,
        Integer founding,
        UUID primarchId,
        String primarchName,
        String homeworld,
        String fortressMonastery,
        boolean codexCompliant,
        Integer approxStrength,
        String description,
        Instant createdAt,
        Instant updatedAt) {
}
