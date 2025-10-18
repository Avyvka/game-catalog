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
import com.github.avyvka.game.catalog.service.DeveloperService;
import com.github.avyvka.game.catalog.service.GameService;
import com.github.avyvka.game.catalog.service.support.AbstractReactiveCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.UUID;

@Service
public class DefaultGameService
        extends AbstractReactiveCrudService<GameEntity, GameDto, UUID>
        implements GameService {

    private final GameGenreRepository gameGenreRepository;

    private final GamePlatformRepository gamePlatformRepository;

    private final GenreMapper genreMapper;

    private final PlatformMapper platformMapper;

    private final DeveloperService developerService;

    @Autowired
    protected DefaultGameService(
            GameRepository gameRepository,
            GameGenreRepository gameGenreRepository,
            GamePlatformRepository gamePlatformRepository,
            GameMapper gameMapper,
            GenreMapper genreMapper,
            PlatformMapper platformMapper,
            DeveloperService developerService
    ) {
        super(gameRepository, gameMapper);
        this.gameGenreRepository = gameGenreRepository;
        this.gamePlatformRepository = gamePlatformRepository;
        this.genreMapper = genreMapper;
        this.platformMapper = platformMapper;
        this.developerService = developerService;
    }

    @Override
    @Transactional
    public Mono<GameDto> create(Mono<GameDto> dtoMono) {
        return dtoMono.zipWhen(dto -> super.create(Mono.just(dto)))
                .flatMap(tuple -> {
                    var source = tuple.getT1();
                    var saved = tuple.getT2();

                    var saveGenres = Mono.justOrEmpty(source.genres())
                            .flatMap(genres -> saveGameGenres(saved.id(), genres));

                    var savePlatforms = Mono.justOrEmpty(source.platforms())
                            .flatMap(platforms -> saveGamePlatforms(saved.id(), platforms));

                    return Mono.when(saveGenres, savePlatforms).thenReturn(saved.id());
                })
                .flatMap(this::findById);
    }

    @Override
    public Mono<GameDto> findById(UUID uuid) {
        var game = super.findById(uuid)
                .zipWhen((e) -> developerService.findById(e.developer().id()));

        var genres = gameGenreRepository.findAllGenreByGameId(uuid)
                .map(genreMapper::toDto);

        var platforms = gamePlatformRepository.findAllPlatformByGameId(uuid)
                .map(platformMapper::toDto);

        return Mono.zip(game, genres.collectList(), platforms.collectList())
                .map(tuple ->
                        new GameDto(
                                tuple.getT1().getT1().id(),
                                tuple.getT1().getT1().name(),
                                tuple.getT1().getT2(),
                                tuple.getT2(),
                                tuple.getT3()
                        )
                );
    }

    @Override
    @Transactional
    public Mono<GameDto> update(UUID uuid, Mono<GameDto> dtoMono) {
        return dtoMono.zipWhen(dto -> super.update(uuid, Mono.just(dto)))
                .flatMap(tuple -> {
                    var dto = tuple.getT1();

                    var updateGenres = gameGenreRepository.deleteAllByGameId(uuid)
                            .then(Mono.justOrEmpty(dto.genres())
                                    .flatMap(genres -> saveGameGenres(uuid, genres))
                            );

                    var updatePlatforms = gamePlatformRepository.deleteAllByGameId(uuid)
                            .then(Mono.justOrEmpty(dto.platforms())
                                    .flatMap(platforms -> saveGamePlatforms(uuid, platforms))
                            );

                    return Mono.when(updateGenres, updatePlatforms);
                })
                .then(this.findById(uuid));
    }

    @Override
    @Transactional
    public Mono<GameDto> partialUpdate(UUID uuid, Mono<GameDto> dtoMono) {
        return dtoMono.zipWhen(dto -> super.partialUpdate(uuid, Mono.just(dto)))
                .flatMap(tuple -> {
                    var dto = tuple.getT1();

                    var updateGenres = Mono.justOrEmpty(dto.genres())
                            .delayUntil(_ -> gameGenreRepository.deleteAllByGameId(uuid))
                            .flatMap(genres -> saveGameGenres(uuid, genres));

                    var updatePlatforms = Mono.justOrEmpty(dto.platforms())
                            .delayUntil(_ -> gamePlatformRepository.deleteAllByGameId(uuid))
                            .flatMap(platforms -> saveGamePlatforms(uuid, platforms));

                    return Mono.when(updateGenres, updatePlatforms);
                })
                .then(this.findById(uuid));
    }

    @Override
    @Transactional
    public Mono<Void> delete(UUID uuid) {
        return gameGenreRepository.deleteAllByGameId(uuid)
                .then(gamePlatformRepository.deleteAllByGameId(uuid))
                .then(super.delete(uuid));
    }

    private Mono<Void> saveGameGenres(UUID gameId, Collection<GenreDto> genres) {
        var gameGenres = genres.stream()
                .map(genre -> new GameGenreEntity(gameId, genre.id()))
                .toList();

        return gameGenreRepository.saveAll(gameGenres).then();
    }

    private Mono<Void> saveGamePlatforms(UUID gameId, Collection<PlatformDto> platforms) {
        var gamePlatforms = platforms.stream()
                .map(platform -> new GamePlatformEntity(gameId, platform.id()))
                .toList();

        return gamePlatformRepository.saveAll(gamePlatforms).then();
    }
}
