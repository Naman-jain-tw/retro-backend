package com.project.retro_backend.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Data
public class CardJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID publicId;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "board_id", nullable = false)
    private BoardJpaEntity board;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
