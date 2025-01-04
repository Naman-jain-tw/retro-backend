package com.project.retro_backend.infrastructure.persistence.adapter;

import com.project.retro_backend.application.port.output.UserRepository;
import com.project.retro_backend.domain.model.User;
import com.project.retro_backend.infrastructure.persistence.entity.UserJpaEntity;
import com.project.retro_backend.infrastructure.persistence.repository.UserJpaRepository;
import com.project.retro_backend.infrastructure.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        UserJpaEntity entity = userMapper.toJpaEntity(user);
        UserJpaEntity savedEntity = userJpaRepository.save(entity);
        return userMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<User> findByPublicId(UUID publicId) {
        return userJpaRepository.findByPublicId(publicId)
                .map(userMapper::toDomainEntity);
    }
} 