package com.project.retro_backend.infrastructure.persistence.adapter;

import com.project.retro_backend.application.port.output.BoardUserRepository;
import com.project.retro_backend.domain.model.BoardUser;
import com.project.retro_backend.infrastructure.persistence.entity.BoardUserJpaEntity;
import com.project.retro_backend.infrastructure.persistence.repository.BoardUserJpaRepository;
import com.project.retro_backend.infrastructure.persistence.mapper.BoardUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BoardUserRepositoryAdapter implements BoardUserRepository {
    private final BoardUserJpaRepository boardUserJpaRepository;
    private final BoardUserMapper boardUserMapper;

    @Override
    public BoardUser save(BoardUser boardUser) {
        BoardUserJpaEntity entity = boardUserMapper.toJpaEntity(boardUser);
        BoardUserJpaEntity savedEntity = boardUserJpaRepository.save(entity);
        return boardUserMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<BoardUser> findByBoardPublicIdAndUserName(UUID boardId, String userName) {
        return boardUserJpaRepository.findByBoardPublicIdAndUserName(boardId, userName)
                .map(boardUserMapper::toDomainEntity);
    }
} 