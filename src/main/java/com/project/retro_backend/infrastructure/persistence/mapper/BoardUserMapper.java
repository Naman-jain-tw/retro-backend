package com.project.retro_backend.infrastructure.persistence.mapper;

import com.project.retro_backend.domain.model.BoardUser;
import com.project.retro_backend.infrastructure.persistence.entity.BoardUserJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardUserMapper {
    private final BoardMapper boardMapper;
    private final UserMapper userMapper;
    
    public BoardUserJpaEntity toJpaEntity(BoardUser domain) {
        BoardUserJpaEntity entity = new BoardUserJpaEntity();
        entity.setId(domain.getId());
        entity.setBoard(boardMapper.toJpaEntity(domain.getBoard()));
        entity.setUser(userMapper.toJpaEntity(domain.getUser()));
        entity.setRole(domain.getRole());
        entity.setStatus(domain.getStatus());
        entity.setJoinedAt(domain.getJoinedAt());
        entity.setLastActiveAt(domain.getLastActiveAt());
        return entity;
    }
    
    public BoardUser toDomainEntity(BoardUserJpaEntity entity) {
        BoardUser domain = new BoardUser();
        domain.setId(entity.getId());
        domain.setBoard(boardMapper.toDomainEntity(entity.getBoard()));
        domain.setUser(userMapper.toDomainEntity(entity.getUser()));
        domain.setRole(entity.getRole());
        domain.setStatus(entity.getStatus());
        domain.setJoinedAt(entity.getJoinedAt());
        domain.setLastActiveAt(entity.getLastActiveAt());
        return domain;
    }
} 