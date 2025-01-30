// package com.project.retro_backend.infrastructure.adapter.input.websocket;

// import com.project.retro_backend.application.service.BoardService;
// import com.project.retro_backend.config.TestWebSocketConfig;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.messaging.Message;
// import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
// import org.springframework.messaging.support.MessageBuilder;
// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.web.socket.WebSocketHandler;
// import org.springframework.web.socket.messaging.SessionDisconnectEvent;

// import java.util.HashMap;
// import java.util.Map;
// import java.util.UUID;

// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.times;

// @SpringBootTest
// @ContextConfiguration(classes = {TestWebSocketConfig.class})
// class BoardWebSocketHandlerTest {

//     @Autowired
//     private WebSocketHandler webSocketHandler;

//     @MockitoBean
//     private BoardService boardService;

//     private UUID testBoardId;
//     private String testUserName;

//     @BeforeEach
//     void setUp() {
//         testBoardId = UUID.randomUUID();
//         testUserName = "Test User";
//     }

//     @Test
//     void handleBoardJoin_ShouldStoreSessionAttributesAndJoinBoard() {
//         // Arrange
//         SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
//         headerAccessor.setSessionAttributes(new HashMap<>());

//         // Act
//         webSocketHandler.handleBoardJoin(testBoardId, testUserName, headerAccessor);

//         // Assert
//         verify(boardService).joinBoard(testBoardId, testUserName);
//         Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
//         assert sessionAttributes.get("boardId").equals(testBoardId.toString());
//         assert sessionAttributes.get("userName").equals(testUserName);
//     }

//     @Test
//     void handleWebSocketDisconnect_ShouldUpdateUserStatus() {
//         // Arrange
//         SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
//         headerAccessor.setSessionId("test-session-123");
        
//         Map<String, Object> sessionAttributes = new HashMap<>();
//         sessionAttributes.put("boardId", testBoardId.toString());
//         sessionAttributes.put("userName", testUserName);
//         headerAccessor.setSessionAttributes(sessionAttributes);

//         Message<byte[]> message = MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders());
        
//         SessionDisconnectEvent event = new SessionDisconnectEvent(
//             "test-source",
//             message,
//             "test-session-123",
//             null
//         );

//         // Act
//         webSocketHandler.handleWebSocketDisconnectListener(event);

//         // Assert
//         verify(boardService, times(1)).handleUserDisconnection(testBoardId, testUserName);
//     }
// } 