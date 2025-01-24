package com.project.retro_backend.infrastructure.adapter.input.websocket;

import com.project.retro_backend.config.TestWebSocketConfig;
import com.project.retro_backend.application.service.BoardService;
import com.project.retro_backend.domain.model.BoardUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
    }
)
@Import(TestWebSocketConfig.class)
class BoardWebSocketIntegrationTest {

    @LocalServerPort
    private int port;

    @MockBean
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
        log.info("Starting WebSocket test with URL: {}", url);

        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                log.info("Connected to WebSocket");
                stompSession = session;
                connectionEstablished.complete(true);

                String destination = "/topic/board/" + testBoardId;
                log.info("Subscribing to: {}", destination);

                StompHeaders subscribeHeaders = new StompHeaders();
                subscribeHeaders.setDestination(destination);
                session.subscribe(subscribeHeaders, new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Map.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        log.info("Received message: {}", payload);
                        try {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> message = (Map<String, Object>) payload;
                            log.info("Message type: {}, userName: {}", 
                                   message.get("type"), message.get("userName"));
                            
                            if ("USER_JOINED".equals(message.get("type")) && 
                                testUserName.equals(message.get("userName"))) {
                                subscriptionComplete.complete(true);
                            }
                        } catch (Exception e) {
                            log.error("Error handling message", e);
                            subscriptionComplete.completeExceptionally(e);
                        }
                    }
                });

                // Send join message
                StompHeaders sendHeaders = new StompHeaders();
                sendHeaders.setDestination("/app/board/" + testBoardId + "/join");
                log.info("Sending join message to: {}", sendHeaders.getDestination());
                session.send(sendHeaders, testUserName);
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                log.error("Transport error", exception);
                connectionEstablished.completeExceptionally(exception);
                subscriptionComplete.completeExceptionally(exception);
            }

            @Override
            public void handleException(StompSession session, StompCommand command,
                                     StompHeaders headers, byte[] payload, Throwable exception) {
                log.error("Handler exception", exception);
                connectionEstablished.completeExceptionally(exception);
                subscriptionComplete.completeExceptionally(exception);
            }
        };

        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("userName", testUserName);

        log.info("Initiating WebSocket connection");
        stompClient.connectAsync(url, new WebSocketHttpHeaders(), connectHeaders, sessionHandler)
            .get(5, TimeUnit.SECONDS); // Wait for connection to be established

        assertTrue(connectionEstablished.get(5, TimeUnit.SECONDS), "WebSocket connection failed");
        //assertTrue(subscriptionComplete.get(10, TimeUnit.SECONDS), "Message subscription/handling failed");
    }
} 