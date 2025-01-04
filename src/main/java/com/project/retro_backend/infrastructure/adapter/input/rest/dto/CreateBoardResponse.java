package com.project.retro_backend.infrastructure.adapter.input.rest.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class CreateBoardResponse {
    private UUID publicId;
    private String name;
} 