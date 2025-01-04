package com.project.retro_backend.infrastructure.persistence.repository;

import com.project.retro_backend.infrastructure.persistence.entity.BoardJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface BoardJpaRepository extends JpaRepository<BoardJpaEntity, Long> {
    Optional<BoardJpaEntity> findByPublicId(UUID publicId);
} 