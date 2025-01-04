package com.project.retro_backend.infrastructure.persistence.repository;

import com.project.retro_backend.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    Optional<UserJpaEntity> findByPublicId(UUID publicId);
} 