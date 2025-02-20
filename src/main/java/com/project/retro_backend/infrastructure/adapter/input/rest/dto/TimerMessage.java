package com.project.retro_backend.infrastructure.adapter.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimerMessage {
    private String status;
    private int remainingSeconds;
    private String formattedTime;
}