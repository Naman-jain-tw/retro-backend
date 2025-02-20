package com.project.retro_backend.application.port.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import com.project.retro_backend.domain.model.Board;
import com.project.retro_backend.infrastructure.persistence.projection.BoardDetailsProjection;

public interface BoardRepository {
    Board save(Board board);

    Optional<Board> findByPublicId(UUID publicId);

    List<BoardDetailsProjection> findBoardDetailsByBoardId(UUID boardId);
}