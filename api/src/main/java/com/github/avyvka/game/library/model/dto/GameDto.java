package com.github.avyvka.game.library.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record GameDto(
        UUID id,
        @NotBlank @Size(max = 32) String name,
        @NotNull DeveloperDto developer,
        List<GenreDto> genres,
        List<PlatformDto> platforms
) {
}
