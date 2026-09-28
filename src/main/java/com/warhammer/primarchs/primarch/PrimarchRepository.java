package com.warhammer.primarchs.primarch;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrimarchRepository extends JpaRepository<Primarch, UUID> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByLegionNumber(Integer legionNumber);

    Page<Primarch> findByAllegiance(Allegiance allegiance, Pageable pageable);
}
