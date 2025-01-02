package com.project.retro_backend.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.UUID;
import java.util.Set;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private UUID publicId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private Set<BoardUser> boardUsers;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
    }
}