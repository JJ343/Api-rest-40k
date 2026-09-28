package com.warhammer.primarchs.primarch;

import com.warhammer.primarchs.common.exception.ConflictException;
import com.warhammer.primarchs.common.exception.ResourceNotFoundException;
import com.warhammer.primarchs.primarch.dto.PrimarchCreateRequest;
import com.warhammer.primarchs.primarch.dto.PrimarchResponse;
import com.warhammer.primarchs.primarch.dto.PrimarchUpdateRequest;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrimarchService {

    private final PrimarchRepository repository;
    private final PrimarchMapper mapper;

    public PrimarchService(PrimarchRepository repository, PrimarchMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public Page<PrimarchResponse> findAll(Allegiance allegiance, Pageable pageable) {
        Page<Primarch> page = allegiance == null
                ? repository.findAll(pageable)
                : repository.findByAllegiance(allegiance, pageable);
        return page.map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public PrimarchResponse findById(UUID id) {
        return mapper.toResponse(getOrThrow(id));
    }

    public PrimarchResponse create(PrimarchCreateRequest request) {
        if (repository.existsByNameIgnoreCase(request.name())) {
            throw new ConflictException("Primarch with name '" + request.name() + "' already exists");
        }
        if (repository.existsByLegionNumber(request.legionNumber())) {
            throw new ConflictException("Primarch with legion number " + request.legionNumber() + " already exists");
        }
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    public PrimarchResponse update(UUID id, PrimarchUpdateRequest request) {
        Primarch primarch = getOrThrow(id);
        if (request.name() != null && !request.name().equalsIgnoreCase(primarch.getName())
                && repository.existsByNameIgnoreCase(request.name())) {
            throw new ConflictException("Primarch with name '" + request.name() + "' already exists");
        }
        if (request.legionNumber() != null && !request.legionNumber().equals(primarch.getLegionNumber())
                && repository.existsByLegionNumber(request.legionNumber())) {
            throw new ConflictException("Primarch with legion number " + request.legionNumber() + " already exists");
        }
        if (request.name() != null) {
            primarch.setName(request.name());
        }
        if (request.legionNumber() != null) {
            primarch.setLegionNumber(request.legionNumber());
        }
        if (request.allegiance() != null) {
            primarch.setAllegiance(request.allegiance());
        }
        if (request.status() != null) {
            primarch.setStatus(request.status());
        }
        if (request.homeworld() != null) {
            primarch.setHomeworld(request.homeworld());
        }
        if (request.description() != null) {
            primarch.setDescription(request.description());
        }
        return mapper.toResponse(repository.save(primarch));
    }

    public void delete(UUID id) {
        repository.delete(getOrThrow(id));
    }

    private Primarch getOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Primarch not found with id " + id));
    }
}
