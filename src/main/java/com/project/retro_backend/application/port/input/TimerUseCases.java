package com.project.retro_backend.application.port.input;

import java.util.UUID;

import com.project.retro_backend.infrastructure.adapter.input.rest.dto.TimerMessage;

public interface TimerUseCases {
    public void startTimer(UUID boardId, int durationMinutes);
    public void stopTimer(UUID boardId);
    public void cancelTimer(UUID boardId);
    public TimerMessage getTimerState(UUID boardId);
}
