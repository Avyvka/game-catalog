package com.github.avyvka.game.library.model.dto;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public record PlatformDto(
        @Id UUID id,
        String name,
        String manufacturer
) {
}
