package com.project.retro_backend.application.service;

import com.project.retro_backend.application.port.input.CreateBoardUseCase;
import com.project.retro_backend.application.port.input.JoinBoardUseCase;
import com.project.retro_backend.application.port.output.BoardRepository;
import com.project.retro_backend.application.port.output.UserRepository;
import com.project.retro_backend.application.port.output.BoardUserRepository;
import com.project.retro_backend.domain.model.Board;
import com.project.retro_backend.domain.model.BoardUser;
import com.project.retro_backend.domain.model.User;
import com.project.retro_backend.domain.model.UserRole;
import com.project.retro_backend.domain.exception.BoardNotFoundException;
import com.project.retro_backend.domain.model.BoardUserStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardService implements CreateBoardUseCase, JoinBoardUseCase {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardUserRepository boardUserRepository;

    @Override
    @Transactional
    public Board createBoard(String name, String creatorName) {
        User admin = new User();
        admin.setName(creatorName);
        admin = userRepository.save(admin);
        
        Board board = new Board();
        board.setName(name);
        board = boardRepository.save(board);
        
        BoardUser boardUser = new BoardUser();
        boardUser.setBoard(board);
        boardUser.setUser(admin);
        boardUser.setRole(UserRole.ADMIN);
        boardUser.setStatus(BoardUserStatus.ACTIVE);
        boardUser.setLastActiveAt(LocalDateTime.now());
        boardUserRepository.save(boardUser);
        
        return board;
    }

    @Override
    @Transactional
    public BoardUser joinBoard(UUID boardId, String userName) {
        // Check if user already joined
        Optional<BoardUser> existingBoardUser = boardUserRepository.findByBoardPublicIdAndUserName(boardId, userName);
        
        if (existingBoardUser.isPresent()) {
            // User is rejoining - update their status
            BoardUser boardUser = existingBoardUser.get();
            boardUser.setStatus(BoardUserStatus.ACTIVE);
            boardUser.setLastActiveAt(LocalDateTime.now());
            return boardUserRepository.save(boardUser);
        }

        // New user joining
        Board board = boardRepository.findByPublicId(boardId)
            .orElseThrow(() -> new BoardNotFoundException("Board not found"));
            
        User user = new User();
        user.setName(userName);
        user = userRepository.save(user);
        
        BoardUser boardUser = new BoardUser();
        boardUser.setBoard(board);
        boardUser.setUser(user);
        boardUser.setRole(UserRole.USER);
        boardUser.setStatus(BoardUserStatus.ACTIVE);
        boardUser.setLastActiveAt(LocalDateTime.now());
        
        return boardUserRepository.save(boardUser);
    }

    // Add method to handle user disconnection
    @Transactional
    public void handleUserDisconnection(UUID boardId, String userName) {
        boardUserRepository.findByBoardPublicIdAndUserName(boardId, userName)
            .ifPresent(boardUser -> {
                boardUser.setStatus(BoardUserStatus.INACTIVE);
                boardUser.setLastActiveAt(LocalDateTime.now());
                boardUserRepository.save(boardUser);
            });
    }
} 