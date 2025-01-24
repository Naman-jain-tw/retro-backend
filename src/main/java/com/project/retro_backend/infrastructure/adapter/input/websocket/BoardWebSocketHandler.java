package com.project.retro_backend.infrastructure.adapter.input.websocket;

import com.project.retro_backend.application.service.BoardService;
import com.project.retro_backend.domain.model.BoardUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.UUID;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardWebSocketHandler {
    private final BoardService boardService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/board/{boardId}/join")
    @SendTo("/topic/board/{boardId}")
    public Map<String, Object> handleBoardJoin(@DestinationVariable UUID boardId, 
                                             String userName,
                                             SimpMessageHeaderAccessor headerAccessor) {
        log.info("Handling board join request for board: {}, user: {}", boardId, userName);
        
        // Store user data in WebSocket session
        if (headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().put("boardId", boardId.toString());
            headerAccessor.getSessionAttributes().put("userName", userName);
        }
        
        // Update user status to active
        BoardUser boardUser = boardService.joinBoard(boardId, userName);
        
        // Notify all users in the board about the new user
        return Map.of(
            "type", "USER_JOINED",
            "userName", userName,
            "timestamp", System.currentTimeMillis(),
            "activeUsers", boardService.getActiveBoardUsers(boardId)
        );
    }

    @MessageMapping("/board/{boardId}/message")
    @SendTo("/topic/board/{boardId}")
    public Map<String, Object> handleBoardMessage(@DestinationVariable UUID boardId,
                                                String userName,
                                                String message) {
        return Map.of(
            "type", "MESSAGE",
            "userName", userName,
            "message", message,
            "timestamp", System.currentTimeMillis()
        );
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        String userName = (String) headerAccessor.getSessionAttributes().get("userName");
        String boardId = (String) headerAccessor.getSessionAttributes().get("boardId");
        
        if (userName != null && boardId != null) {
            UUID boardUUID = UUID.fromString(boardId);
            boardService.handleUserDisconnection(boardUUID, userName);
            
            // Notify others about user disconnection
            messagingTemplate.convertAndSend(
                "/topic/board/" + boardId,
                Map.of(
                    "type", "USER_LEFT",
                    "userName", userName,
                    "timestamp", System.currentTimeMillis(),
                    "activeUsers", boardService.getActiveBoardUsers(boardUUID)
                )
            );
        }
    }
} 