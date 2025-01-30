package com.project.retro_backend.infrastructure.adapter.input.websocket;

public class CardContent {
    private String cardContent;

    public CardContent() {
        // Default constructor is required for Jackson deserialization
    }

    public String getCardContent() {
        return cardContent;
    }

    public void setCardContent(String cardContent) {
        this.cardContent = cardContent;
    }
}
