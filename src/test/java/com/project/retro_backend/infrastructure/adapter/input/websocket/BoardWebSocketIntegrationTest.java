package com.project.retro_backend.infrastructure.adapter.input.websocket;

import com.project.retro_backend.config.TestWebSocketConfig;
import com.project.retro_backend.application.service.BoardService;
import com.project.retro_backend.domain.model.BoardUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
})
@Import(TestWebSocketConfig.class)
class BoardWebSocketIntegrationTest {

    @LocalServerPort
    private int port;

    @MockitoBean
    private BoardService boardService;

    private WebSocketStompClient stompClient;
    private StompSession stompSession;
    private final CompletableFuture<Boolean> connectionEstablished = new CompletableFuture<>();
    private final CompletableFuture<Boolean> subscriptionComplete = new CompletableFuture<>();

    @BeforeEach
    void setUp() {
        WebSocketClient client = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        when(boardService.joinBoard(any(), any())).thenReturn(new BoardUser());
    }

    @AfterEach
    void tearDown() throws Exception {
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.disconnect();
        }
        if (stompClient != null) {
            stompClient.stop();
        }
    }

    @Test
    void testWebSocketConnection() throws ExecutionException, InterruptedException, TimeoutException {
        String url = String.format("ws://localhost:%d/ws", port);
        UUID testBoardId = UUID.randomUUID();
        String testUserName = "Test User";

        // Mock the BoardService methods
        when(boardService.validateToken(eq("testToken"), eq(testBoardId))).thenReturn(true);
        Set<String> activeUsers = new HashSet<>(Set.of("Test User", "user2"));
        when(boardService.getActiveBoardUsers(eq(testBoardId))).thenReturn(activeUsers);

        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                stompSession = session;
                connectionEstablished.complete(true);

                // Subscribe to the topic
                String destination = "/topic/board/" + testBoardId;
                session.subscribe(destination, new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Map.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> message = (Map<String, Object>) payload;

                        if ("USER_JOINED".equals(message.get("type")) &&
                                testUserName.equals(message.get("userName"))) {
                            subscriptionComplete.complete(true);
                        }
                    }
                });

                // Send join message
                session.send("/app/board/" + testBoardId + "/join", testUserName);
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                connectionEstablished.completeExceptionally(exception);
                subscriptionComplete.completeExceptionally(exception);
            }
        };

        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("token", "testToken");
        connectHeaders.add("boardId", testBoardId.toString());
        connectHeaders.add("userName", testUserName);

        stompClient.connect(url, new WebSocketHttpHeaders(), connectHeaders, sessionHandler);

        assertTrue(connectionEstablished.get(5, TimeUnit.SECONDS), "WebSocket connection failed");
        // assertTrue(subscriptionComplete.get(5, TimeUnit.SECONDS), "Message subscription/handling failed");
    }

}