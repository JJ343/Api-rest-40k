package com.warhammer.primarchs.primarch;

import com.warhammer.primarchs.primarch.dto.PrimarchCreateRequest;
import com.warhammer.primarchs.primarch.dto.PrimarchResponse;
import org.springframework.stereotype.Component;

@Component
public class PrimarchMapper {

    public Primarch toEntity(PrimarchCreateRequest request) {
        return new Primarch(
                request.name(),
                request.legionNumber(),
                request.allegiance(),
                request.status(),
                request.homeworld(),
                request.description());
    }

    public PrimarchResponse toResponse(Primarch primarch) {
        return new PrimarchResponse(
                primarch.getId(),
                primarch.getName(),
                primarch.getLegionNumber(),
                primarch.getAllegiance(),
                primarch.getStatus(),
                primarch.getHomeworld(),
                primarch.getDescription(),
                primarch.getCreatedAt(),
                primarch.getUpdatedAt());
    }
}
