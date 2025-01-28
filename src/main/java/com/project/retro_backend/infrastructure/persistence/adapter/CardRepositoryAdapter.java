package com.project.retro_backend.infrastructure.persistence.adapter;

import com.project.retro_backend.application.port.output.CardRepository;
import com.project.retro_backend.domain.model.Card;
import com.project.retro_backend.infrastructure.persistence.entity.CardJpaEntity;
import com.project.retro_backend.infrastructure.persistence.mapper.CardMapper;
import com.project.retro_backend.infrastructure.persistence.repository.CardJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardRepositoryAdapter implements CardRepository {
    private final CardJpaRepository cardJpaRepository;
    private final CardMapper cardMapper;

    @Override
    public Card save(Card card) {
        CardJpaEntity entity = cardMapper.toJpaEntity(card);
        CardJpaEntity savedEntity = cardJpaRepository.save(entity);
        return cardMapper.toDomainEntity(savedEntity);
    }
}
