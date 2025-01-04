package com.project.retro_backend.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Set;

@Entity
@Table(name = "boards")
@Data
public class BoardJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private UUID publicId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private Set<BoardUserJpaEntity> boardUsers;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
    }
} 