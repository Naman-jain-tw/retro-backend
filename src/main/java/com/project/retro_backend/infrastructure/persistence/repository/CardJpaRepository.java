package com.project.retro_backend.infrastructure.persistence.repository;

import com.project.retro_backend.infrastructure.persistence.entity.CardJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardJpaRepository extends JpaRepository<CardJpaEntity, Long> {
}
