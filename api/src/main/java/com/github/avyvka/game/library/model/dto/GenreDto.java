package com.github.avyvka.game.library.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record GenreDto(
        UUID id,
        @NotBlank @Size(max = 32) String name
) {
}
