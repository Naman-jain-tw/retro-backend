package com.project.retro_backend.controller;

import com.project.retro_backend.domain.*;
import com.project.retro_backend.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest {

    @Mock
    private BoardService boardService;

    @InjectMocks
    private BoardController boardController;

    private Board testBoard;
    private BoardUser testBoardUser;

    @BeforeEach
    void setUp() {
        testBoard = new Board();
        testBoard.setName("Test Board");
        testBoard.setPublicId(UUID.randomUUID());

        User testUser = new User();
        testUser.setName("Test User");
        testUser.setPublicId(UUID.randomUUID());

        testBoardUser = new BoardUser();
        testBoardUser.setBoard(testBoard);
        testBoardUser.setUser(testUser);
        testBoardUser.setRole(UserRole.USER);
    }

    @Test
    void createBoard_ShouldReturnCreatedBoard() {
        // Arrange
        when(boardService.createBoard(anyString(), anyString())).thenReturn(testBoard);

        // Act
        ResponseEntity<Board> response = boardController.createBoard("Test Board", "Test User");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Test Board");
    }

    @Test
    void joinBoard_ShouldReturnBoardUser() {
        // Arrange
        when(boardService.joinBoard(any(UUID.class), anyString())).thenReturn(testBoardUser);

        // Act
        ResponseEntity<BoardUser> response = boardController.joinBoard(UUID.randomUUID(), "Test User");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUser().getName()).isEqualTo("Test User");
        assertThat(response.getBody().getRole()).isEqualTo(UserRole.USER);
    }
} 