package com.github.avyvka.game.library.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("game")
public record GameEntity(
        @Id UUID id,
        String name,
        UUID developerId
) {
}
