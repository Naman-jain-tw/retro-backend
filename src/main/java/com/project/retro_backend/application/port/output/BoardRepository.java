package com.project.retro_backend.application.port.output;

import java.util.Optional;
import java.util.UUID;

import com.project.retro_backend.domain.model.Board;

public interface BoardRepository {
    Board save(Board board);

    Optional<Board> findByPublicId(UUID publicId);
}