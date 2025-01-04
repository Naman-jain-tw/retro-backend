package com.project.retro_backend.infrastructure.persistence.repository;

import com.project.retro_backend.infrastructure.persistence.entity.BoardUserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

public interface BoardUserJpaRepository extends JpaRepository<BoardUserJpaEntity, Long> {
    @Query("SELECT bu FROM BoardUserJpaEntity bu WHERE bu.board.publicId = :boardId AND bu.user.name = :userName")
    Optional<BoardUserJpaEntity> findByBoardPublicIdAndUserName(@Param("boardId") UUID boardId, @Param("userName") String userName);
} 