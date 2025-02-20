package com.project.retro_backend.infrastructure.persistence.repository;

import com.project.retro_backend.infrastructure.persistence.entity.BoardJpaEntity;
import com.project.retro_backend.infrastructure.persistence.projection.BoardDetailsProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoardJpaRepository extends JpaRepository<BoardJpaEntity, Long> {
    Optional<BoardJpaEntity> findByPublicId(UUID publicId);

    @Query(value = "SELECT b.public_id AS boardId, b.name AS boardName, c.text AS text, " +
            "c.column_type AS columnType, u.name AS userName, u.public_id AS userPublicId " +
            "FROM boards b " +
            "JOIN cards c ON c.board_id = b.id " +
            "JOIN users u ON u.id = c.user_id " +
            "WHERE b.public_id = :boardId", nativeQuery = true)
    List<BoardDetailsProjection> findBoardDetailsByBoardId(@Param("boardId") UUID boardId);

}