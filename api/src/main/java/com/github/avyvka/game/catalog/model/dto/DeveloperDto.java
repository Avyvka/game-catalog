package com.github.avyvka.game.catalog.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DeveloperDto(
        UUID id,
        @NotBlank @Size(max = 32) String name,
        String description
) {
}