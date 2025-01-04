package com.project.retro_backend.application.port.output;

import java.util.Optional;
import java.util.UUID;

import com.project.retro_backend.domain.model.User;

public interface UserRepository {
    User save(User user);
    Optional<User> findByPublicId(UUID publicId);
} 