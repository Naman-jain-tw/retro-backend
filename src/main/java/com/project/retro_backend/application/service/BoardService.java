package com.project.retro_backend.application.service;

import com.project.retro_backend.application.port.input.CreateBoardUseCase;
import com.project.retro_backend.application.port.input.JoinBoardUseCase;
import com.project.retro_backend.application.port.output.BoardRepository;
import com.project.retro_backend.application.port.output.UserRepository;
import com.project.retro_backend.application.port.output.BoardUserRepository;
import com.project.retro_backend.application.port.output.UserTokenRepository;
import com.project.retro_backend.domain.model.Board;
import com.project.retro_backend.domain.model.BoardUser;
import com.project.retro_backend.domain.model.User;
import com.project.retro_backend.domain.model.UserRole;
import com.project.retro_backend.domain.exception.BoardNotFoundException;
import com.project.retro_backend.domain.model.BoardUserStatus;
import com.project.retro_backend.domain.model.UserToken;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Primary
public class BoardService implements CreateBoardUseCase, JoinBoardUseCase {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardUserRepository boardUserRepository;
    private final UserTokenRepository userTokenRepository;
    private final Map<UUID, Set<String>> activeBoardUsers = new ConcurrentHashMap<>();

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
        Board board = boardRepository.findByPublicId(boardId)
            .orElseThrow(() -> new BoardNotFoundException("Board not found"));

        // Check if user already joined
        Optional<BoardUser> existingBoardUser = boardUserRepository.findByBoardPublicIdAndUserName(boardId, userName);
        
        BoardUser boardUser;
        if (existingBoardUser.isPresent()) {
            // User is rejoining - update their status
            boardUser = existingBoardUser.get();
            boardUser.setStatus(BoardUserStatus.ACTIVE);
            boardUser.setLastActiveAt(LocalDateTime.now());
            boardUser = boardUserRepository.save(boardUser);
        } else {
            // New user joining
            User user = new User();
            user.setName(userName);
            user = userRepository.save(user);
            
            boardUser = new BoardUser();
            boardUser.setBoard(board);
            boardUser.setUser(user);
            boardUser.setRole(UserRole.USER);
            boardUser.setStatus(BoardUserStatus.ACTIVE);
            boardUser.setLastActiveAt(LocalDateTime.now());
            boardUser = boardUserRepository.save(boardUser);
        }

        // Generate and save token
        UserToken userToken = generateUserToken(boardUser.getUser(), board);
        
        // Track active user
        activeBoardUsers.computeIfAbsent(boardId, k -> ConcurrentHashMap.newKeySet())
                       .add(userName);
        
        return boardUser;
    }

    public UserToken generateUserToken(User user, Board board) {
        // Deactivate any existing active tokens for this user and board
        userTokenRepository.findByUserIdAndBoardIdAndActiveTrue(user.getId(), board.getId())
            .ifPresent(existingToken -> {
                existingToken.setActive(false);
                userTokenRepository.save(existingToken);
            });

        // Create new token
        UserToken token = new UserToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setBoard(board);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusHours(24)); // Token expires in 24 hours
        token.setActive(true);

        return userTokenRepository.save(token);
    }

    // Add method to validate token
    public boolean validateToken(String token, UUID boardId) {
        return userTokenRepository.findByToken(token)
            .map(userToken -> 
                userToken.isActive() &&
                userToken.getBoard().getPublicId().equals(boardId) &&
                userToken.getExpiresAt().isAfter(LocalDateTime.now())
            )
            .orElse(false);
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

        // Remove user from active users
        activeBoardUsers.computeIfPresent(boardId, (k, users) -> {
            users.remove(userName);
            return users.isEmpty() ? null : users;
        });
    }

    public Set<String> getActiveBoardUsers(UUID boardId) {
        return activeBoardUsers.getOrDefault(boardId, Collections.emptySet());
    }
} 