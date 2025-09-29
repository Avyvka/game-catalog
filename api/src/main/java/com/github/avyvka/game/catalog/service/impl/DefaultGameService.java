package com.github.avyvka.game.catalog.service.impl;

import com.github.avyvka.game.catalog.mapper.GameMapper;
import com.github.avyvka.game.catalog.mapper.GenreMapper;
import com.github.avyvka.game.catalog.mapper.PlatformMapper;
import com.github.avyvka.game.catalog.model.dto.GameDto;
import com.github.avyvka.game.catalog.model.dto.GenreDto;
import com.github.avyvka.game.catalog.model.dto.PlatformDto;
import com.github.avyvka.game.catalog.model.entity.GameEntity;
import com.github.avyvka.game.catalog.model.entity.GameGenreEntity;
import com.github.avyvka.game.catalog.model.entity.GamePlatformEntity;
import com.github.avyvka.game.catalog.repository.GameGenreRepository;
import com.github.avyvka.game.catalog.repository.GamePlatformRepository;
import com.github.avyvka.game.catalog.repository.GameRepository;
import com.github.avyvka.game.catalog.service.GameService;
import com.github.avyvka.game.catalog.service.support.AbstractReactiveCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.UUID;

@Service
public class DefaultGameService
        extends AbstractReactiveCrudService<GameEntity, GameDto, UUID>
        implements GameService
{
    private final GameGenreRepository gameGenreRepository;

    private final GamePlatformRepository gamePlatformRepository;

    private final GenreMapper genreMapper;

    private final PlatformMapper platformMapper;

    @Autowired
    protected DefaultGameService(
            GameRepository gameRepository,
            GameGenreRepository gameGenreRepository,
            GamePlatformRepository gamePlatformRepository,
            GameMapper gameMapper,
            GenreMapper genreMapper,
            PlatformMapper platformMapper
    ) {
        super(gameRepository, gameMapper);
        this.gameGenreRepository = gameGenreRepository;
        this.gamePlatformRepository = gamePlatformRepository;
        this.genreMapper = genreMapper;
        this.platformMapper = platformMapper;
    }

    @Override
    @Transactional
    public Mono<GameDto> create(Mono<GameDto> dtoMono) {
        return dtoMono.flatMap(dto ->
                super.create(Mono.just(dto))
                        .delayUntil(saved -> saveGameGenres(saved.id(), dto.genres()))
                        .delayUntil(saved -> saveGamePlatforms(saved.id(), dto.platforms()))
                        .flatMap(saved -> this.findById(saved.id()))
        );
    }

    @Override
    public Mono<GameDto> findById(UUID uuid) {
        var game = super.findById(uuid);

        var genres = gameGenreRepository.findAllGenreByGameId(uuid)
                .map(genreMapper::toDto);

        var platforms = gamePlatformRepository.findAllPlatformByGameId(uuid)
                .map(platformMapper::toDto);

        return Mono.zip(game, genres.collectList(), platforms.collectList())
                .map(tuple ->
                        new GameDto(
                                tuple.getT1().id(),
                                tuple.getT1().name(),
                                tuple.getT1().developer(),
                                tuple.getT2(),
                                tuple.getT3()
                        )
                );
    }

    @Override
    @Transactional
    public Mono<GameDto> update(UUID uuid, Mono<GameDto> dtoMono) {
        return dtoMono.flatMap(dto ->
                super.update(uuid, Mono.just(dto))
                        .delayUntil(e -> gameGenreRepository.deleteAllByGameId(uuid))
                        .delayUntil(e -> saveGameGenres(uuid, dto.genres()))
                        .delayUntil(e -> gamePlatformRepository.deleteAllByGameId(uuid))
                        .delayUntil(e -> saveGamePlatforms(uuid, dto.platforms()))
                        .flatMap(e -> this.findById(e.id()))
        );
    }

    @Override
    @Transactional
    public Mono<GameDto> partialUpdate(UUID uuid, Mono<GameDto> dtoMono) {
        return dtoMono.flatMap(dto ->
                super.partialUpdate(uuid, Mono.just(dto))
                        .delayUntil(e -> gameGenreRepository.deleteAllByGameId(uuid))
                        .delayUntil(e -> saveGameGenres(uuid, dto.genres()))
                        .delayUntil(e -> gamePlatformRepository.deleteAllByGameId(uuid))
                        .delayUntil(e -> saveGamePlatforms(uuid, dto.platforms()))
                        .flatMap(e -> this.findById(e.id()))
        );
    }

    private Mono<Void> saveGameGenres(UUID gameId, @Nullable Collection<GenreDto> genres) {
        if (genres == null) {
            return Mono.empty();
        }

        var gameGenres = genres.stream()
                .map(genre -> new GameGenreEntity(gameId, genre.id()))
                .toList();

        return gameGenreRepository.saveAll(gameGenres).then();
    }

    private Mono<Void> saveGamePlatforms(UUID gameId, @Nullable Collection<PlatformDto> platforms) {
        if (platforms == null) {
            return Mono.empty();
        }

        var gamePlatforms = platforms.stream()
                .map(platform -> new GamePlatformEntity(gameId, platform.id()))
                .toList();

        return gamePlatformRepository.saveAll(gamePlatforms).then();
    }
}
