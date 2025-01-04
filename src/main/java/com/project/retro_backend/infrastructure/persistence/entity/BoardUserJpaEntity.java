package com.project.retro_backend.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.project.retro_backend.domain.model.UserRole;
import com.project.retro_backend.domain.model.BoardUserStatus;

@Entity
@Table(name = "board_users")
@Data
public class BoardUserJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private BoardJpaEntity board;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    
    @Column(nullable = false)
    private LocalDateTime joinedAt;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardUserStatus status;
    
    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt;
    
    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
    }
} 