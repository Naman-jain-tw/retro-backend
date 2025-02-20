package com.project.retro_backend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private Board board;
    private User user;
    public String columnType;
}
