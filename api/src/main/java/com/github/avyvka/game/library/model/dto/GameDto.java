package com.github.avyvka.game.library.model.dto;

import java.util.List;
import java.util.UUID;

public record GameDto(
        UUID id,
        String name,
        DeveloperDto developer,
        List<GenreDto> genres,
        List<PlatformDto> platforms
) {
}
