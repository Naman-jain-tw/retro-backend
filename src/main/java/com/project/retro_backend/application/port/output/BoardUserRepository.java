package com.project.retro_backend.application.port.output;

import com.project.retro_backend.domain.model.BoardUser;
import java.util.Optional;
import java.util.UUID;

public interface BoardUserRepository {
    BoardUser save(BoardUser boardUser);

    Optional<BoardUser> findByBoardPublicIdAndUserName(UUID boardId, String userName);
}