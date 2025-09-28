package com.github.avyvka.game.library.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GameDto(
        UUID id,
        @NotBlank @Size(max = 32) String name,
        @NotNull DeveloperDto developer,
        List<GenreDto> genres,
        List<PlatformDto> platforms
) {
}
