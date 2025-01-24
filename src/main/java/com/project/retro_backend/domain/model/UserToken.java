package com.project.retro_backend.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserToken {
    private Long id;
    private String token;
    private User user;
    private Board board;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean active;
}