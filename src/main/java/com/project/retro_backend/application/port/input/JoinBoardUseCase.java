package com.project.retro_backend.application.port.input;

import java.util.UUID;

import com.project.retro_backend.domain.model.Board;
import com.project.retro_backend.domain.model.BoardUser;
import com.project.retro_backend.domain.model.User;
import com.project.retro_backend.domain.model.UserToken;

public interface JoinBoardUseCase {
    BoardUser joinBoard(UUID boardId, String userName);

    UserToken generateUserToken(User user, Board board);
} 