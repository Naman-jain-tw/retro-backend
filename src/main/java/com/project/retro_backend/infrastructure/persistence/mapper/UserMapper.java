package com.project.retro_backend.infrastructure.persistence.mapper;

import com.project.retro_backend.domain.model.User;
import com.project.retro_backend.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    public UserJpaEntity toJpaEntity(User domain) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(domain.getId());
        entity.setPublicId(domain.getPublicId());
        entity.setName(domain.getName());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }
    
    public User toDomainEntity(UserJpaEntity entity) {
        User domain = new User();
        domain.setId(entity.getId());
        domain.setPublicId(entity.getPublicId());
        domain.setName(entity.getName());
        domain.setCreatedAt(entity.getCreatedAt());
        return domain;
    }
} 