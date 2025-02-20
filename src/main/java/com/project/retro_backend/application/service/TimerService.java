package com.project.retro_backend.application.service;

import com.project.retro_backend.application.port.input.TimerUseCases;
import com.project.retro_backend.domain.exception.BoardNotFoundException;
import com.project.retro_backend.infrastructure.adapter.input.rest.dto.TimerMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class TimerService implements TimerUseCases {
    private final BoardService boardService;
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<UUID, ScheduledFuture<?>> activeTimers = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> remainingTimes = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void startTimer(UUID boardId, int durationMinutes) {
        if (!boardService.boardExists(boardId)) {
            throw new BoardNotFoundException("Board not found");
        }

        // Validate duration
        if (durationMinutes < 2 || durationMinutes > 60) {
            throw new IllegalArgumentException("Duration must be between 2 and 60 minutes");
        }

        // Cancel existing timer if any
        cancelTimer(boardId);

        // Convert minutes to seconds
        int durationSeconds = durationMinutes * 60;
        remainingTimes.put(boardId, durationSeconds);

        // Schedule timer updates
        ScheduledFuture<?> timerFuture = scheduler.scheduleAtFixedRate(() -> {
            int remaining = remainingTimes.get(boardId);
            if (remaining <= 0) {
                handleTimerCompletion(boardId);
                return;
            }

            remaining--;
            remainingTimes.put(boardId, remaining);
            broadcastRemainingTime(boardId, remaining);
        }, 0, 1, TimeUnit.SECONDS);

        activeTimers.put(boardId, timerFuture);
    }

    public void stopTimer(UUID boardId) {
        ScheduledFuture<?> timer = activeTimers.get(boardId);
        if (timer != null) {
            timer.cancel(false);
            broadcastRemainingTime(boardId, remainingTimes.get(boardId));
        }
    }

    public void cancelTimer(UUID boardId) {
        ScheduledFuture<?> timer = activeTimers.remove(boardId);
        if (timer != null) {
            timer.cancel(true);
            remainingTimes.remove(boardId);
            broadcastTimerCancelled(boardId);
        }
    }

    private void handleTimerCompletion(UUID boardId) {
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

    // Clean up method to be called on application shutdown
    public void shutdown() {
        scheduler.shutdown();
        activeTimers.forEach((boardId, timer) -> timer.cancel(true));
        activeTimers.clear();
        remainingTimes.clear();
    }

    public TimerMessage getTimerState(UUID boardId) {
        Integer remainingSeconds = remainingTimes.get(boardId);
        if (remainingSeconds == null) {
            return new TimerMessage("NO_TIMER", 0, "00:00");
        }
        return new TimerMessage(
                "TIMER_UPDATE",
                remainingSeconds,
                formatTime(remainingSeconds));
    }
}
