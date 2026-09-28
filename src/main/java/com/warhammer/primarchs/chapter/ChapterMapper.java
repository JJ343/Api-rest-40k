package com.warhammer.primarchs.chapter;

import com.warhammer.primarchs.chapter.dto.ChapterCreateRequest;
import com.warhammer.primarchs.chapter.dto.ChapterResponse;
import com.warhammer.primarchs.primarch.Primarch;
import org.springframework.stereotype.Component;

@Component
public class ChapterMapper {

    public Chapter toEntity(ChapterCreateRequest request, Primarch primarch) {
        return new Chapter(
                request.name(),
                request.founding(),
                primarch,
                request.homeworld(),
                request.fortressMonastery(),
                request.codexCompliant(),
                request.approxStrength(),
                request.description());
    }

    public ChapterResponse toResponse(Chapter chapter) {
        return new ChapterResponse(
                chapter.getId(),
                chapter.getName(),
                chapter.getFounding(),
                chapter.getPrimarch().getId(),
                chapter.getPrimarch().getName(),
                chapter.getHomeworld(),
                chapter.getFortressMonastery(),
                chapter.isCodexCompliant(),
                chapter.getApproxStrength(),
                chapter.getDescription(),
                chapter.getCreatedAt(),
                chapter.getUpdatedAt());
    }
}
