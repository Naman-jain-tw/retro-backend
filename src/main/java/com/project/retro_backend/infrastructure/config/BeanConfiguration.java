package com.project.retro_backend.infrastructure.config;

import com.project.retro_backend.application.port.output.BoardRepository;
import com.project.retro_backend.application.port.output.UserRepository;
import com.project.retro_backend.application.port.output.UserTokenRepository;
import com.project.retro_backend.application.port.output.BoardUserRepository;
import com.project.retro_backend.application.service.BoardService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
} 