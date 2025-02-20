package com.project.retro_backend.infrastructure.persistence.projection;

import java.util.UUID;

public interface BoardDetailsProjection {
    byte[] getBoardId();

    String getBoardName();

    String getText();

    String getColumnType();

    String getUserName();

    byte[] getUserPublicId();
}
