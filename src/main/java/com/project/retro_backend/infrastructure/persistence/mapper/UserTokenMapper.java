package com.project.retro_backend.infrastructure.persistence.mapper;

import com.project.retro_backend.domain.model.UserToken;
import com.project.retro_backend.infrastructure.persistence.entity.UserTokenJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserTokenMapper {
    
    private final UserMapper userMapper;
    private final BoardMapper boardMapper;
    
    public UserTokenJpaEntity toJpaEntity(UserToken domain) {
        UserTokenJpaEntity entity = new UserTokenJpaEntity();
        entity.setId(domain.getId());
        entity.setToken(domain.getToken());
        entity.setUser(userMapper.toJpaEntity(domain.getUser()));
        entity.setBoard(boardMapper.toJpaEntity(domain.getBoard()));
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setActive(domain.isActive());
        return entity;
    }
    
    public UserToken toDomainEntity(UserTokenJpaEntity entity) {
        UserToken domain = new UserToken();
        domain.setId(entity.getId());
        domain.setToken(entity.getToken());
        domain.setUser(userMapper.toDomainEntity(entity.getUser()));
        domain.setBoard(boardMapper.toDomainEntity(entity.getBoard()));
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setExpiresAt(entity.getExpiresAt());
        domain.setActive(entity.isActive());
        return domain;
    }
}
