package com.project.retro_backend.application.service;

import com.project.retro_backend.application.port.input.CreateCardUseCase;
import com.project.retro_backend.application.port.output.BoardRepository;
import com.project.retro_backend.application.port.output.BoardUserRepository;
import com.project.retro_backend.application.port.output.CardRepository;
import com.project.retro_backend.application.port.output.UserRepository;
import com.project.retro_backend.domain.exception.BoardNotFoundException;
import com.project.retro_backend.domain.exception.UserNotFoundException;
import com.project.retro_backend.domain.model.Board;
import com.project.retro_backend.domain.model.BoardUser;
import com.project.retro_backend.domain.model.Card;
import com.project.retro_backend.infrastructure.adapter.input.websocket.CardContent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Primary
public class CardService implements CreateCardUseCase {
    private final CardRepository cardRepository;
    private final BoardRepository boardRepository;
    private final BoardUserRepository boardUserRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public Card createCard(UUID boardId, String userName, String column, String message) {
        // Check if board exists
        Board board = boardRepository.findByPublicId(boardId)
                .orElseThrow(() -> new BoardNotFoundException("Board not found"));

        // Check if user exists and has joined the board
        BoardUser boardUser = boardUserRepository.findByBoardPublicIdAndUserName(boardId, userName)
                .orElseThrow(() -> new UserNotFoundException("User not found or not part of the board"));

        // Create and save the card
        Card card = new Card();
        card.setText(message);
        card.setBoard(board);
        card.setUser(boardUser.getUser());
        card.setCreatedAt(LocalDateTime.now());
        card.setColumnType(column);

        cardRepository.save(card);
        notifyFrontend(message, column, boardId);

        return card;
    }

    public void notifyFrontend(final String message, final String columnType, UUID boardId) {
        CardContent content = new CardContent(message, columnType);
        messagingTemplate.convertAndSend("/topic/board" + boardId + "/messages", content);
    }
}