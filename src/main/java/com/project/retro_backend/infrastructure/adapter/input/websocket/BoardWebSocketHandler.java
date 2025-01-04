package com.project.retro_backend.infrastructure.adapter.input.websocket;

import com.project.retro_backend.application.service.BoardService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class BoardWebSocketHandler {
    private final BoardService boardService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        String userName = (String) headerAccessor.getSessionAttributes().get("userName");
        String boardId = (String) headerAccessor.getSessionAttributes().get("boardId");
        
        if (userName != null && boardId != null) {
            boardService.handleUserDisconnection(UUID.fromString(boardId), userName);
        }
    }
} 