package com.warhammer.primarchs.primarch;

import com.warhammer.primarchs.primarch.dto.PrimarchCreateRequest;
import com.warhammer.primarchs.primarch.dto.PrimarchResponse;
import com.warhammer.primarchs.primarch.dto.PrimarchUpdateRequest;
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
@RequestMapping("/api/v1/primarchs")
@Tag(name = "Primarchs", description = "Primarchs of the Warhammer 40k universe")
public class PrimarchController {

    private final PrimarchService service;

    public PrimarchController(PrimarchService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List Primarchs whit  pagination and allegiance filter")
    public Page<PrimarchResponse> findAll(
            @RequestParam(required = false) Allegiance allegiance,
            @PageableDefault(size = 20, sort = "legionNumber") Pageable pageable) {
        return service.findAll(allegiance, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtain a Primarch by its ID")
    public PrimarchResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a Primarch (requires authentication)")
    public PrimarchResponse create(@Valid @RequestBody PrimarchCreateRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Primarch (requires authentication)")
    public PrimarchResponse update(@PathVariable UUID id, @Valid @RequestBody PrimarchUpdateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Primarch (requires authentication)")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
