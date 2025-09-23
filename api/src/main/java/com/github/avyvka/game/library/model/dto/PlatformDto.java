package com.github.avyvka.game.library.model.dto;

import com.github.avyvka.game.library.model.Identifiable;
import org.springframework.data.annotation.Id;

import java.util.UUID;

public record PlatformDto(
        @Id UUID id,
        String name,
        String manufacturer
) implements Identifiable<UUID> {
}
