package com.project.retro_backend.application.service;

import com.project.retro_backend.application.port.input.TimerUseCases;
import com.project.retro_backend.domain.exception.BoardNotFoundException;
import com.project.retro_backend.domain.model.TimerState;
import com.project.retro_backend.infrastructure.adapter.input.rest.dto.TimerMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimerService implements TimerUseCases {
    private final BoardService boardService;
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<UUID, TimerState> timerStates = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void startTimer(UUID boardId, int durationMinutes) {
        log.info("Starting timer for board: {} with duration: {} minutes", boardId, durationMinutes);
        
        if (!boardService.boardExists(boardId)) {
            throw new BoardNotFoundException("Board not found");
        }

        if (durationMinutes < 2 || durationMinutes > 60) {
            throw new IllegalArgumentException("Duration must be between 2 and 60 minutes");
        }

        cancelTimer(boardId);

        int durationSeconds = durationMinutes * 60;
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            TimerState state = timerStates.get(boardId);
            if (state == null || state.getRemainingSeconds() <= 0) {
                handleTimerCompletion(boardId);
                return;
            }

            state.setRemainingSeconds(state.getRemainingSeconds() - 1);
            broadcastRemainingTime(boardId, state.getRemainingSeconds());
        }, 0, 1, TimeUnit.SECONDS);

        timerStates.put(boardId, new TimerState(durationSeconds, future));
        log.info("Timer started successfully for board: {}", boardId);
    }

    public TimerMessage getTimerState(UUID boardId) {
        log.info("Getting timer state for board: {}", boardId);
        
        if (!boardService.boardExists(boardId)) {
            throw new BoardNotFoundException("Board not found");
        }

        TimerState state = timerStates.get(boardId);
        if (state == null) {
            return new TimerMessage("NO_TIMER", 0, "00:00");
        }

        return new TimerMessage(
            state.getStatus(),
            state.getRemainingSeconds(),
            formatTime(state.getRemainingSeconds())
        );
    }

    public void stopTimer(UUID boardId) {
        log.info("Stopping timer for board: {}", boardId);
        TimerState state = timerStates.get(boardId);
        if (state != null) {
            state.getFuture().cancel(false);
            state.setStatus("STOPPED");
            broadcastRemainingTime(boardId, state.getRemainingSeconds());
        }
    }

    public void cancelTimer(UUID boardId) {
        log.info("Canceling timer for board: {}", boardId);
        TimerState state = timerStates.remove(boardId);
        if (state != null) {
            state.getFuture().cancel(true);
            broadcastTimerCancelled(boardId);
        }
    }

    private void handleTimerCompletion(UUID boardId) {
        log.info("Timer completed for board: {}", boardId);
        cancelTimer(boardId);
        broadcastTimerComplete(boardId);
    }

    private void broadcastRemainingTime(UUID boardId, int remainingSeconds) {
        TimerMessage message = new TimerMessage(
                "TIMER_UPDATE",
                remainingSeconds,
                formatTime(remainingSeconds));
        messagingTemplate.convertAndSend("/topic/board/" + boardId + "/timer", message);
    }

    private void broadcastTimerComplete(UUID boardId) {
        TimerMessage message = new TimerMessage("TIMER_COMPLETE", 0, "00:00");
        messagingTemplate.convertAndSend("/topic/board/" + boardId + "/timer", message);
    }

    private void broadcastTimerCancelled(UUID boardId) {
        TimerMessage message = new TimerMessage("TIMER_CANCELLED", 0, "00:00");
        messagingTemplate.convertAndSend("/topic/board/" + boardId + "/timer", message);
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void shutdown() {
        log.info("Shutting down timer service");
        scheduler.shutdown();
        timerStates.forEach((boardId, state) -> state.getFuture().cancel(true));
        timerStates.clear();
    }
}
