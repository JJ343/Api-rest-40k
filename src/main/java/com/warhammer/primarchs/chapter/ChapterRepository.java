package com.warhammer.primarchs.chapter;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<Chapter, UUID> {

    boolean existsByNameIgnoreCase(String name);

    Page<Chapter> findByPrimarchId(UUID primarchId, Pageable pageable);
}
