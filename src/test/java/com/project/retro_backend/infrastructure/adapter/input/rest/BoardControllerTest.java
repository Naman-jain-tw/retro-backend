// package com.project.retro_backend.infrastructure.adapter.input.rest;

// import com.project.retro_backend.application.service.BoardService;
// import com.project.retro_backend.domain.model.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;

// import java.time.LocalDateTime;
// import java.util.UUID;

// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// @WebMvcTest(BoardController.class)
// class BoardControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockitoBean
//     private BoardService boardService;

//     private Board testBoard;
//     private BoardUser testBoardUser;

//     @BeforeEach
//     void setUp() {
//         testBoard = new Board();
//         testBoard.setId(1L);
//         testBoard.setPublicId(UUID.randomUUID());
//         testBoard.setName("Test Board");
//         testBoard.setCreatedAt(LocalDateTime.now());

//         User testUser = new User();
//         testUser.setId(1L);
//         testUser.setPublicId(UUID.randomUUID());
//         testUser.setName("Test User");
//         testUser.setCreatedAt(LocalDateTime.now());

//         testBoardUser = new BoardUser();
//         testBoardUser.setId(1L);
//         testBoardUser.setBoard(testBoard);
//         testBoardUser.setUser(testUser);
//         testBoardUser.setRole(UserRole.USER);
//         testBoardUser.setStatus(BoardUserStatus.ACTIVE);
//         testBoardUser.setLastActiveAt(LocalDateTime.now());
//     }

//     @Test
//     void createBoard_ShouldReturnCreatedBoard() throws Exception {
//         // Arrange
//         when(boardService.createBoard(anyString(), anyString())).thenReturn(testBoard);

//         // Act & Assert
//         mockMvc.perform(post("/api/boards")
//                 .param("name", "Test Board")
//                 .param("creatorName", "Test User")
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.publicId").value(testBoard.getPublicId().toString()))
//                 .andExpect(jsonPath("$.name").value("Test Board"));
//     }

//     @Test
//     void joinBoard_ShouldReturnBoardUser() throws Exception {
//         // Arrange
//         UUID testBoardId = UUID.randomUUID();
//         UserToken mockToken = new UserToken();
//         mockToken.setToken("sample-token");
//         when(boardService.joinBoard(eq(testBoardId), anyString())).thenReturn(testBoardUser);
//         when(boardService.generateUserToken(any(User.class), any(Board.class))).thenReturn(mockToken);

//         // Act & Assert
//         mockMvc.perform(post("/api/boards/{boardId}/join", testBoardId)
//                 .param("userName", "Test User")
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.boardUser.user.publicId").value(testBoardUser.getUser().getPublicId().toString()))
//                 .andExpect(jsonPath("$.boardUser.user.name").value("Test User"))
//                 .andExpect(jsonPath("$.boardUser.role").value("USER"))
//                 .andExpect(jsonPath("$.boardUser.status").value("ACTIVE"))
//                 .andExpect(jsonPath("$.token").value("sample-token"))
//                 .andExpect(jsonPath("$.wsEndpoint").value("/ws"))
//                 .andExpect(jsonPath("$.boardTopic").value("/topic/board/" + testBoardId))
//                 .andExpect(jsonPath("$.joinEndpoint").value("/app/board/" + testBoardId + "/join"));
//     }
// }
