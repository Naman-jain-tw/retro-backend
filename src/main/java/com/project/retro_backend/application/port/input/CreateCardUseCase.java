package com.project.retro_backend.application.port.input;

import com.project.retro_backend.domain.model.Card;

import java.util.UUID;

public interface CreateCardUseCase {
    Card createCard(UUID boardId, String userName, String text);
}
