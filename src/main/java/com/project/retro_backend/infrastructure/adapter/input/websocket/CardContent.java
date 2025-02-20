package com.project.retro_backend.infrastructure.adapter.input.websocket;

public class CardContent {
    private String cardContent;

    private String columnType;

    public CardContent() {
        // Default constructor is required for Jackson deserialization
    }

    public CardContent(String cardContent, String columnType) {
        this.cardContent = cardContent;
        this.columnType = columnType;
    }

    public String getCardContent() {
        return cardContent;
    }

    public void setCardContent(String cardContent) {
        this.cardContent = cardContent;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }
}
