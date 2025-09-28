package com.github.avyvka.game.library.model.entity;

import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("game_genre")
public record GameGenreEntity(
        UUID gameId,
        UUID genreId
) {
}
