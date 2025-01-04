package com.project.retro_backend.infrastructure.persistence.mapper;

import com.project.retro_backend.domain.model.Board;
import com.project.retro_backend.infrastructure.persistence.entity.BoardJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class BoardMapper {
    
    public BoardJpaEntity toJpaEntity(Board domain) {
        BoardJpaEntity entity = new BoardJpaEntity();
        entity.setId(domain.getId());
        entity.setPublicId(domain.getPublicId());
        entity.setName(domain.getName());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }
    
    public Board toDomainEntity(BoardJpaEntity entity) {
        Board domain = new Board();
        domain.setId(entity.getId());
        domain.setPublicId(entity.getPublicId());
        domain.setName(entity.getName());
        domain.setCreatedAt(entity.getCreatedAt());
        return domain;
    }
} 