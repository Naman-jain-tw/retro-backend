package com.project.retro_backend.repository;

import com.project.retro_backend.domain.BoardUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {
} 