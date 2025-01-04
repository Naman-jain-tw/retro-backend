package com.project.retro_backend.application.service;

import com.project.retro_backend.application.port.output.*;
import com.project.retro_backend.domain.model.*;
import com.project.retro_backend.domain.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BoardUserRepository boardUserRepository;

    private BoardService boardService;
    private Board testBoard;
    private User testUser;
    private BoardUser testBoardUser;

    @BeforeEach
    void setUp() {
        boardService = new BoardService(boardRepository, userRepository, boardUserRepository);
        
        testBoard = new Board();
        testBoard.setId(1L);
        testBoard.setPublicId(UUID.randomUUID());
        testBoard.setName("Test Board");
        testBoard.setCreatedAt(LocalDateTime.now());

        testUser = new User();
        testUser.setId(1L);
        testUser.setPublicId(UUID.randomUUID());
        testUser.setName("Test User");
        testUser.setCreatedAt(LocalDateTime.now());

        testBoardUser = new BoardUser();
        testBoardUser.setId(1L);
        testBoardUser.setBoard(testBoard);
        testBoardUser.setUser(testUser);
        testBoardUser.setRole(UserRole.ADMIN);
        testBoardUser.setStatus(BoardUserStatus.ACTIVE);
        testBoardUser.setLastActiveAt(LocalDateTime.now());
    }

    @Test
    void createBoard_ShouldCreateBoardWithAdmin() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(boardRepository.save(any(Board.class))).thenReturn(testBoard);
        when(boardUserRepository.save(any(BoardUser.class))).thenReturn(testBoardUser);

        // Act
        Board result = boardService.createBoard("Test Board", "Test User");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Board");
    }

    @Test
    void joinBoard_ShouldAddUserToExistingBoard() {
        // Arrange
        when(boardRepository.findByPublicId(any(UUID.class))).thenReturn(Optional.of(testBoard));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(boardUserRepository.save(any(BoardUser.class))).thenReturn(testBoardUser);
        when(boardUserRepository.findByBoardPublicIdAndUserName(any(UUID.class), any(String.class)))
            .thenReturn(Optional.empty());

        // Act
        BoardUser result = boardService.joinBoard(UUID.randomUUID(), "Test User");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUser().getName()).isEqualTo("Test User");
        assertThat(result.getStatus()).isEqualTo(BoardUserStatus.ACTIVE);
    }

    @Test
    void joinBoard_ShouldReactivateUser_WhenUserRejoins() {
        // Arrange
        testBoardUser.setStatus(BoardUserStatus.INACTIVE);
        when(boardUserRepository.findByBoardPublicIdAndUserName(any(UUID.class), any(String.class)))
            .thenReturn(Optional.of(testBoardUser));
        when(boardUserRepository.save(any(BoardUser.class))).thenReturn(testBoardUser);

        // Act
        BoardUser result = boardService.joinBoard(UUID.randomUUID(), "Test User");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(BoardUserStatus.ACTIVE);
    }

    @Test
    void joinBoard_ShouldThrowException_WhenBoardNotFound() {
        // Arrange
        UUID nonExistentBoardId = UUID.randomUUID();
        when(boardRepository.findByPublicId(nonExistentBoardId)).thenReturn(Optional.empty());
        when(boardUserRepository.findByBoardPublicIdAndUserName(any(UUID.class), any(String.class)))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BoardNotFoundException.class, () -> 
            boardService.joinBoard(nonExistentBoardId, "Test User")
        );
    }

    @Test
    void handleUserDisconnection_ShouldUpdateUserStatus() {
        // Arrange
        UUID boardId = UUID.randomUUID();
        String userName = "Test User";
        when(boardUserRepository.findByBoardPublicIdAndUserName(boardId, userName))
            .thenReturn(Optional.of(testBoardUser));
        when(boardUserRepository.save(any(BoardUser.class))).thenReturn(testBoardUser);

        // Act
        boardService.handleUserDisconnection(boardId, userName);

        // Assert
        assertThat(testBoardUser.getStatus()).isEqualTo(BoardUserStatus.INACTIVE);
    }
} 