package com.github.avyvka.game.library.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("platform")
public record PlatformEntity(
        @Id UUID id,
        String name,
        String manufacturer
) {
}
