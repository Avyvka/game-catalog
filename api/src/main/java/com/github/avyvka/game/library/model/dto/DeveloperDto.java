package com.github.avyvka.game.library.model.dto;

import java.util.UUID;

public record DeveloperDto(
        UUID id,
        String name,
        String description
) {
}