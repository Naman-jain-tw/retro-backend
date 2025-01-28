package com.project.retro_backend.application.port.output;

import com.project.retro_backend.domain.model.Card;

public interface CardRepository {
    Card save(Card card);
}
