package com.project.retro_backend.application.port.input;

import com.project.retro_backend.domain.model.Board;

public interface CreateBoardUseCase {
    Board createBoard(String name, String creatorName);
} 