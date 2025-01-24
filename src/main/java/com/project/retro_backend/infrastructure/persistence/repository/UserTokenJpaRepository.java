package com.project.retro_backend.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.retro_backend.domain.model.UserToken;
import com.project.retro_backend.infrastructure.persistence.entity.UserTokenJpaEntity;

public interface UserTokenJpaRepository extends JpaRepository<UserTokenJpaEntity, Long> {
    Optional<UserTokenJpaEntity> findByToken(String token);

    Optional<UserTokenJpaEntity> findByUserIdAndBoardIdAndActiveTrue(Long userId, Long boardId);

    UserToken save(UserToken userToken);

}
