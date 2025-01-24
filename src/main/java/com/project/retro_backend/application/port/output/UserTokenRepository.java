package com.project.retro_backend.application.port.output;

import com.project.retro_backend.domain.model.UserToken;

import java.util.Optional;

public interface UserTokenRepository {
    Optional<UserToken> findByToken(String token);

    Optional<UserToken> findByUserIdAndBoardIdAndActiveTrue(Long userId, Long boardId);

    UserToken save(UserToken userToken);
}