package com.github.avyvka.game.library.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;

import java.util.UUID;

public record PlatformDto(
        @Id UUID id,
        @NotBlank @Size(max = 32) String name,
        @NotBlank @Size(max = 32) String manufacturer
) {
}
