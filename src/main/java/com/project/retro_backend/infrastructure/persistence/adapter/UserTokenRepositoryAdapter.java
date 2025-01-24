package com.project.retro_backend.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.project.retro_backend.application.port.output.UserTokenRepository;
import com.project.retro_backend.domain.model.UserToken;
import com.project.retro_backend.infrastructure.persistence.mapper.UserTokenMapper;
import com.project.retro_backend.infrastructure.persistence.repository.UserTokenJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserTokenRepositoryAdapter implements UserTokenRepository {

    private final UserTokenJpaRepository userTokenJpaRepository;
    private final UserTokenMapper userTokenMapper;

    @Override
    public Optional<UserToken> findByToken(String token) {
        return userTokenJpaRepository.findByToken(token)
                .map(userTokenMapper::toDomainEntity);
    }

    @Override
    public Optional<UserToken> findByUserIdAndBoardIdAndActiveTrue(Long userId, Long boardId) {
        return userTokenJpaRepository.findByUserIdAndBoardIdAndActiveTrue(userId, boardId)
                .map(userTokenMapper::toDomainEntity);
    }

    @Override
    public UserToken save(UserToken userToken) {
        return userTokenMapper.toDomainEntity(userTokenJpaRepository.save(userTokenMapper.toJpaEntity(userToken)));
    }

}
