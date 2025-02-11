package com.project.retro_backend.infrastructure.config;

import com.project.retro_backend.application.port.output.*;
import com.project.retro_backend.application.service.BoardService;
import com.project.retro_backend.application.service.CardService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class BeanConfiguration {

    @Bean
    public BoardService boardService(
            BoardRepository boardRepository,
            UserRepository userRepository,
            BoardUserRepository boardUserRepository,
            UserTokenRepository userTokenRepository) {
        return new BoardService(boardRepository, userRepository, boardUserRepository, userTokenRepository);
    }

    @Bean
    public CardService cardService(
            CardRepository cardRepository,
            BoardRepository boardRepository,
            BoardUserRepository boardUserRepository,
            SimpMessagingTemplate simpMessagingTemplate) {
        return new CardService(cardRepository, boardRepository, boardUserRepository, simpMessagingTemplate);
    }
} 