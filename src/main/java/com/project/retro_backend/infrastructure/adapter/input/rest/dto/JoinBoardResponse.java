package com.project.retro_backend.infrastructure.adapter.input.rest.dto;

import com.project.retro_backend.domain.model.UserRole;
import com.project.retro_backend.domain.model.BoardUserStatus;
import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
public class JoinBoardResponse {
    private UUID userPublicId;
    private String userName;
    private UserRole role;
    private BoardUserStatus status;
    private LocalDateTime lastActiveAt;
} 