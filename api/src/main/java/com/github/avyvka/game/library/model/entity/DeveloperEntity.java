package com.github.avyvka.game.library.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("developer")
public record DeveloperEntity(
        @Id UUID id,
        String name,
        String description
) {
}
