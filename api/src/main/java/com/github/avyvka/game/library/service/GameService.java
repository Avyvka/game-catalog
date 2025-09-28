package com.github.avyvka.game.library.service;

import com.github.avyvka.game.library.mapper.GameMapper;
import com.github.avyvka.game.library.mapper.GenreMapper;
import com.github.avyvka.game.library.mapper.PlatformMapper;
import com.github.avyvka.game.library.model.dto.GameDto;
import com.github.avyvka.game.library.model.dto.GenreDto;
import com.github.avyvka.game.library.model.dto.PlatformDto;
import com.github.avyvka.game.library.model.entity.GameEntity;
import com.github.avyvka.game.library.model.entity.GameGenreEntity;
import com.github.avyvka.game.library.model.entity.GamePlatformEntity;
import com.github.avyvka.game.library.repository.GameGenreRepository;
import com.github.avyvka.game.library.repository.GamePlatformRepository;
import com.github.avyvka.game.library.repository.GameRepository;
import com.github.avyvka.game.library.service.support.AbstractReactiveCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.UUID;

@Service
public class GameService extends AbstractReactiveCrudService<GameEntity, GameDto, UUID> {

    private final GameGenreRepository gameGenreRepository;

    private final GamePlatformRepository gamePlatformRepository;

    private final GenreMapper genreMapper;

    private final PlatformMapper platformMapper;

    @Autowired
    protected GameService(
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
    public Mono<GameDto> create(Mono<GameDto> dto) {
        return dto.flatMap(this::create)
                .flatMap(saved -> findById(saved.id()));
    }

    private Mono<GameDto> create(GameDto dto) {
        return super.create(Mono.just(dto))
                .flatMap(savedGame ->
                        saveGameGenres(savedGame.id(), dto.genres()).thenReturn(savedGame)
                )
                .flatMap(savedGame ->
                        saveGamePlatforms(savedGame.id(), dto.platforms()).thenReturn(savedGame)
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
    public Mono<GameDto> update(UUID uuid, Mono<GameDto> dto) {
        return dto.flatMap(gameDto -> this.update(uuid, gameDto))
                .then(findById(uuid));
    }

    private Mono<Void> update(UUID uuid, GameDto dto) {
        return super.update(uuid, Mono.just(dto))
                .then(gameGenreRepository.deleteAllByGameId(uuid))
                .then(saveGameGenres(uuid, dto.genres()))
                .then(gamePlatformRepository.deleteAllByGameId(uuid))
                .then(saveGamePlatforms(uuid, dto.platforms()));
    }

    @Override
    @Transactional
    public Mono<GameDto> partialUpdate(UUID uuid, Mono<GameDto> dto) {
        return dto.flatMap(gameDto -> this.partialUpdate(uuid, gameDto))
                .then(findById(uuid));
    }

    private Mono<Void> partialUpdate(UUID uuid, GameDto dto) {
        return super.partialUpdate(uuid, Mono.just(dto))
                .then(gameGenreRepository.deleteAllByGameId(uuid))
                .then(saveGameGenres(uuid, dto.genres()))
                .then(gamePlatformRepository.deleteAllByGameId(uuid))
                .then(saveGamePlatforms(uuid, dto.platforms()));
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
