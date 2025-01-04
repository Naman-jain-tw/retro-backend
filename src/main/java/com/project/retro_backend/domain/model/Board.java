package com.project.retro_backend.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    private Long id;
    private UUID publicId;
    private String name;
    private LocalDateTime createdAt;
    private Set<BoardUser> boardUsers;
}