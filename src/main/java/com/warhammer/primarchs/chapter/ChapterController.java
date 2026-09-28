package com.warhammer.primarchs.chapter;

import com.warhammer.primarchs.chapter.dto.ChapterCreateRequest;
import com.warhammer.primarchs.chapter.dto.ChapterResponse;
import com.warhammer.primarchs.chapter.dto.ChapterUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chapters")
@Tag(name = "Chapters", description = "Chapters of Space Marines descending from the Primarchs")
public class ChapterController {

    private final ChapterService service;

    public ChapterController(ChapterService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List Chapters with pagination and filter by Primarch")
    public Page<ChapterResponse> findAll(
            @RequestParam(required = false) UUID primarchId,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return service.findAll(primarchId, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtain a Chapter by its ID")
    public ChapterResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a Chapter (requires authentication)")
    public ChapterResponse create(@Valid @RequestBody ChapterCreateRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Chapter (requires authentication)")
    public ChapterResponse update(@PathVariable UUID id, @Valid @RequestBody ChapterUpdateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Chapter (requires authentication)")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
