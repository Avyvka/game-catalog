package com.github.avyvka.game.catalog.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlatformDto(
        UUID id,
        @NotBlank @Size(max = 32) String name,
        @NotBlank @Size(max = 32) String manufacturer
) {
}
