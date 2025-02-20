package com.project.retro_backend.application.port.input;

import java.util.UUID;

import com.project.retro_backend.infrastructure.adapter.input.rest.dto.BoardDetailsResponse;

public interface GetBoardDetailsUseCase {

    BoardDetailsResponse getBoardDetails(UUID boardId);
}
