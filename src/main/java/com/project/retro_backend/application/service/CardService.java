package com.project.retro_backend.application.service;

import com.project.retro_backend.application.port.input.CreateCardUseCase;
import com.project.retro_backend.domain.model.Card;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class CardService implements CreateCardUseCase {
    @Override
    public Card createCard() {
        return null;
    }
}
