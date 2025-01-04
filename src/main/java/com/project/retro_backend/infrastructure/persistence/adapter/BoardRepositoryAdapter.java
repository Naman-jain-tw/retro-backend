package com.project.retro_backend.infrastructure.persistence.adapter;

import com.project.retro_backend.application.port.output.BoardRepository;
import com.project.retro_backend.domain.model.Board;
import com.project.retro_backend.infrastructure.persistence.entity.BoardJpaEntity;
import com.project.retro_backend.infrastructure.persistence.repository.BoardJpaRepository;
import com.project.retro_backend.infrastructure.persistence.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BoardRepositoryAdapter implements BoardRepository {
    private final BoardJpaRepository boardJpaRepository;
    private final BoardMapper boardMapper;

    @Override
    public Board save(Board board) {
        BoardJpaEntity entity = boardMapper.toJpaEntity(board);
        BoardJpaEntity savedEntity = boardJpaRepository.save(entity);
        return boardMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Board> findByPublicId(UUID publicId) {
        return boardJpaRepository.findByPublicId(publicId)
                .map(boardMapper::toDomainEntity);
    }
} 