package com.warhammer.primarchs.chapter;

import com.warhammer.primarchs.chapter.dto.ChapterCreateRequest;
import com.warhammer.primarchs.chapter.dto.ChapterResponse;
import com.warhammer.primarchs.chapter.dto.ChapterUpdateRequest;
import com.warhammer.primarchs.common.exception.ConflictException;
import com.warhammer.primarchs.common.exception.ResourceNotFoundException;
import com.warhammer.primarchs.primarch.Primarch;
import com.warhammer.primarchs.primarch.PrimarchRepository;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final PrimarchRepository primarchRepository;
    private final ChapterMapper mapper;

    public ChapterService(ChapterRepository chapterRepository,
                          PrimarchRepository primarchRepository,
                          ChapterMapper mapper) {
        this.chapterRepository = chapterRepository;
        this.primarchRepository = primarchRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public Page<ChapterResponse> findAll(UUID primarchId, Pageable pageable) {
        Page<Chapter> page = primarchId == null
                ? chapterRepository.findAll(pageable)
                : chapterRepository.findByPrimarchId(primarchId, pageable);
        return page.map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ChapterResponse findById(UUID id) {
        return mapper.toResponse(getOrThrow(id));
    }

    public ChapterResponse create(ChapterCreateRequest request) {
        if (chapterRepository.existsByNameIgnoreCase(request.name())) {
            throw new ConflictException("Chapter with name '" + request.name() + "' already exists");
        }
        Primarch primarch = primarchRepository.findById(request.primarchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Primarch not found with id " + request.primarchId()));
        return mapper.toResponse(chapterRepository.save(mapper.toEntity(request, primarch)));
    }

    public ChapterResponse update(UUID id, ChapterUpdateRequest request) {
        Chapter chapter = getOrThrow(id);
        if (request.name() != null && !request.name().equalsIgnoreCase(chapter.getName())
                && chapterRepository.existsByNameIgnoreCase(request.name())) {
            throw new ConflictException("Chapter with name '" + request.name() + "' already exists");
        }
        if (request.name() != null) {
            chapter.setName(request.name());
        }
        if (request.founding() != null) {
            chapter.setFounding(request.founding());
        }
        if (request.primarchId() != null) {
            Primarch primarch = primarchRepository.findById(request.primarchId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Primarch not found with id " + request.primarchId()));
            chapter.setPrimarch(primarch);
        }
        if (request.homeworld() != null) {
            chapter.setHomeworld(request.homeworld());
        }
        if (request.fortressMonastery() != null) {
            chapter.setFortressMonastery(request.fortressMonastery());
        }
        if (request.codexCompliant() != null) {
            chapter.setCodexCompliant(request.codexCompliant());
        }
        if (request.approxStrength() != null) {
            chapter.setApproxStrength(request.approxStrength());
        }
        if (request.description() != null) {
            chapter.setDescription(request.description());
        }
        return mapper.toResponse(chapterRepository.save(chapter));
    }

    public void delete(UUID id) {
        chapterRepository.delete(getOrThrow(id));
    }

    private Chapter getOrThrow(UUID id) {
        return chapterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found with id " + id));
    }
}
