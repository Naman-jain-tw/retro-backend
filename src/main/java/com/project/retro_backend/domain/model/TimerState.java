package com.project.retro_backend.domain.model;

import lombok.Data;
import java.util.concurrent.ScheduledFuture;

@Data
public class TimerState {
    private final int initialDuration;
    private int remainingSeconds;
    private ScheduledFuture<?> future;
    private String status;

    public TimerState(int durationSeconds, ScheduledFuture<?> future) {
        this.initialDuration = durationSeconds;
        this.remainingSeconds = durationSeconds;
        this.future = future;
        this.status = "RUNNING";
    }
}