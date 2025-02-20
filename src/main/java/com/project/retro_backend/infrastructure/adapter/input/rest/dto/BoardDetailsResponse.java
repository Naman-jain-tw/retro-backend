package com.project.retro_backend.infrastructure.adapter.input.rest.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class BoardDetailsResponse {
    private UUID boardId;
    private String boardName;
    private List<CardDetails> cards;

    @Data
    public static class CardDetails {
        private String text;
        private String columnType;
        private UserDetails user;

        @Data
        public static class UserDetails {
            private String name;
            private UUID publicId;
        }
    }
}