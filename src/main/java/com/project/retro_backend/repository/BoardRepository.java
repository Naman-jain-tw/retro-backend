package com.project.retro_backend.repository;

import com.project.retro_backend.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByPublicId(UUID publicId);
} 