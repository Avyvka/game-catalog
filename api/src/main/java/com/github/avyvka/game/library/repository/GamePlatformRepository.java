package com.github.avyvka.game.library.repository;

import com.github.avyvka.game.library.model.entity.GamePlatformEntity;
import com.github.avyvka.game.library.model.entity.PlatformEntity;
import com.github.avyvka.game.library.repository.api.CustomR2dbcRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface GamePlatformRepository extends CustomR2dbcRepository<GamePlatformEntity, Void> {

    @Query("""
           SELECT platform.* FROM platform \
           JOIN game_platform ON platform.id = game_platform.platform_id \
           WHERE game_platform.game_id = :gameId\
           """)
    Flux<PlatformEntity> findAllPlatformByGameId(@Param("gameId") UUID gameId);

    Mono<Void> deleteAllByGameId(UUID gameId);
}
