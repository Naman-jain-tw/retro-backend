package com.project.retro_backend.application.port.input;

import java.util.UUID;

import com.project.retro_backend.domain.model.BoardUser;

public interface JoinBoardUseCase {
    BoardUser joinBoard(UUID boardId, String userName);
} 