package com.github.avyvka.game.catalog.repository;

import com.github.avyvka.game.catalog.model.entity.GameGenreEntity;
import com.github.avyvka.game.catalog.model.entity.GenreEntity;
import com.github.avyvka.game.catalog.repository.api.CustomR2dbcRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface GameGenreRepository extends CustomR2dbcRepository<GameGenreEntity, Void> {

    @Query("""
           SELECT genre.* FROM genre \
           JOIN game_genre ON genre.id = game_genre.genre_id \
           WHERE game_genre.game_id = :gameId\
           """)
    Flux<GenreEntity> findAllGenreByGameId(@Param("gameId") UUID gameId);

    Mono<Void> deleteAllByGameId(UUID gameId);
}
