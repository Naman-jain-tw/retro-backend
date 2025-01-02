package com.project.retro_backend.service;

import com.project.retro_backend.domain.*;
import com.project.retro_backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    private BoardService boardService;

    private Board testBoard;
    private User testUser;
    private BoardUser testBoardUser;

    @BeforeEach
    void setUp() {
        testBoard = new Board();
        testBoard.setName("Test Board");
        testBoard.setPublicId(UUID.randomUUID());

        testUser = new User();
        testUser.setName("Test User");
        testUser.setPublicId(UUID.randomUUID());

        testBoardUser = new BoardUser();
        testBoardUser.setBoard(testBoard);
        testBoardUser.setUser(testUser);
        testBoardUser.setRole(UserRole.ADMIN);
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
        BoardUser joinBoardUser = new BoardUser();
        joinBoardUser.setBoard(testBoard);
        joinBoardUser.setUser(testUser);
        joinBoardUser.setRole(UserRole.USER);

        when(boardRepository.findByPublicId(any(UUID.class))).thenReturn(Optional.of(testBoard));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(boardUserRepository.save(any(BoardUser.class))).thenReturn(joinBoardUser);

        // Act
        BoardUser result = boardService.joinBoard(UUID.randomUUID(), "Test User");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUser().getName()).isEqualTo("Test User");
        assertThat(result.getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void joinBoard_ShouldThrowException_WhenBoardNotFound() {
        // Arrange
        UUID nonExistentBoardId = UUID.randomUUID();
        when(boardRepository.findByPublicId(nonExistentBoardId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            boardService.joinBoard(nonExistentBoardId, "Test User")
        );
    }
} 