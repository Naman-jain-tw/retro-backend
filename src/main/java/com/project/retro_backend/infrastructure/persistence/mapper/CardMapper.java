package com.project.retro_backend.infrastructure.persistence.mapper;

import com.project.retro_backend.domain.model.Card;
import com.project.retro_backend.infrastructure.persistence.entity.CardJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardMapper {
    private final BoardMapper boardMapper;
    private final UserMapper userMapper;

    public CardJpaEntity toJpaEntity(Card domain) {
        CardJpaEntity entity = new CardJpaEntity();
        entity.setId(domain.getId());
        entity.setText(domain.getText());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setColumnType(domain.getColumnType());
        entity.setUser(userMapper.toJpaEntity(domain.getUser()));
        entity.setBoard(boardMapper.toJpaEntity(domain.getBoard()));
        return entity;
    }

    public Card toDomainEntity(CardJpaEntity entity) {
        Card domain = new Card();
        domain.setId(entity.getId());
        domain.setText(entity.getText());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setColumnType(entity.getColumnType());
        domain.setUser(userMapper.toDomainEntity(entity.getUser()));
        domain.setBoard(boardMapper.toDomainEntity(entity.getBoard()));
        return domain;
    }
}
