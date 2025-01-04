package com.project.retro_backend.domain.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BoardUser {
    private Long id;
    private Board board;
    private User user;
    private UserRole role;
    private BoardUserStatus status;
    private LocalDateTime joinedAt;
    private LocalDateTime lastActiveAt;
} 