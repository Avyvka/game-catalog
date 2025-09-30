package com.github.avyvka.game.catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.avyvka.game.catalog.model.dto.DeveloperDto;
import com.github.avyvka.game.catalog.model.dto.GameDto;
import com.github.avyvka.game.catalog.model.dto.GenreDto;
import com.github.avyvka.game.catalog.model.dto.PlatformDto;
import com.github.avyvka.game.catalog.model.entity.*;
import com.github.avyvka.game.catalog.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebFlux
@AutoConfigureWebTestClient
class GameReactiveCrudControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private GameGenreRepository gameGenreRepository;

    @Autowired
    private GamePlatformRepository gamePlatformRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PlatformRepository platformRepository;

    @BeforeEach
    void setup() {
        gameGenreRepository.deleteAll().block();
        gamePlatformRepository.deleteAll().block();
        gameRepository.deleteAll().block();
        developerRepository.deleteAll().block();
        genreRepository.deleteAll().block();
        platformRepository.deleteAll().block();
    }

    @Test
    void create_ShouldReturnCreatedEntity() throws IOException {
        var developer = new DeveloperEntity(null, "developer#1", "description#1");
        var genre = new GenreEntity(null, "genre#1");
        var platform = new PlatformEntity(null, "platform#1", "manufacturer#1");

        var savedDeveloper = developerRepository.save(developer).block();
        var savedGenre = genreRepository.save(genre).block();
        var savedPlatform = platformRepository.save(platform).block();

        assertNotNull(savedDeveloper);
        assertNotNull(savedGenre);
        assertNotNull(savedPlatform);

        var game = new GameDto(
                null,
                "game#1",
                new DeveloperDto(savedDeveloper.id(), null, null),
                List.of(new GenreDto(savedGenre.id(), null)),
                List.of(new PlatformDto(savedPlatform.id(), null, null))
        );

        var response = webClient.post()
                .uri("/api/v1/games")
                .bodyValue(game)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.name").isEqualTo(game.name())
                .jsonPath("$.developer.id").isEqualTo(savedDeveloper.id())
                .jsonPath("$.genres").isArray()
                .jsonPath("$.genres[0].id").isEqualTo(savedGenre.id())
                .jsonPath("$.genres[0].name").isEqualTo(savedGenre.name())
                .jsonPath("$.platforms").isArray()
                .jsonPath("$.platforms[0].id").isEqualTo(savedPlatform.id())
                .jsonPath("$.platforms[0].name").isEqualTo(savedPlatform.name())
                .returnResult()
                .getResponseBody();

        var created = objectMapper.readValue(response, GameDto.class);

        var saved = gameRepository.findById(created.id()).block();

        assertNotNull(saved);
        assertThat(saved.name()).isEqualTo("game#1");
        assertThat(saved.developerId()).isEqualTo(savedDeveloper.id());
    }

    @Test
    void getById_ShouldReturnEntity_WhenFound() {
        var developer = new DeveloperEntity(null, "developer#1", "description#1");
        var genre = new GenreEntity(null, "genre#1");
        var platform = new PlatformEntity(null, "platform#1", "manufacturer#1");

        var savedDeveloper = developerRepository.save(developer).block();
        var savedGenre = genreRepository.save(genre).block();
        var savedPlatform = platformRepository.save(platform).block();

        assertNotNull(savedDeveloper);
        assertNotNull(savedGenre);
        assertNotNull(savedPlatform);

        var game = new GameEntity(null, "game#1", savedDeveloper.id());
        var savedGame = gameRepository.save(game).block();

        assertNotNull(savedGame);

        var gameGenre = new GameGenreEntity(savedGame.id(), savedGenre.id());
        var gamePlatform = new GamePlatformEntity(savedGame.id(), savedPlatform.id());

        gameGenreRepository.save(gameGenre).block();
        gamePlatformRepository.save(gamePlatform).block();

        webClient.get()
                .uri("/api/v1/games/{id}", savedGame.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedGame.id())
                .jsonPath("$.name").isEqualTo("game#1")
                .jsonPath("$.developer.id").isEqualTo(savedDeveloper.id())
                .jsonPath("$.genres").isArray()
                .jsonPath("$.genres[0].id").isEqualTo(savedGenre.id())
                .jsonPath("$.genres[0].name").isEqualTo(savedGenre.name())
                .jsonPath("$.platforms").isArray()
                .jsonPath("$.platforms[0].id").isEqualTo(savedPlatform.id())
                .jsonPath("$.platforms[0].name").isEqualTo(savedPlatform.name());
    }

    @Test
    void getById_ShouldReturnNotFound_WhenEmpty() {
        webClient.get()
                .uri("/api/v1/games/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAll_ShouldReturnList() {
        var developer = new DeveloperEntity(null, "developer#1", "description#1");
        var genre1 = new GenreEntity(null, "genre#1");
        var genre2 = new GenreEntity(null, "genre#2");
        var platform1 = new PlatformEntity(null, "platform#1", "manufacturer#1");
        var platform2 = new PlatformEntity(null, "platform#2", "manufacturer#2");

        var savedDeveloper = developerRepository.save(developer).block();
        var savedGenre1 = genreRepository.save(genre1).block();
        var savedGenre2 = genreRepository.save(genre2).block();
        var savedPlatform1 = platformRepository.save(platform1).block();
        var savedPlatform2 = platformRepository.save(platform2).block();

        assertNotNull(savedDeveloper);
        assertNotNull(savedGenre1);
        assertNotNull(savedGenre2);
        assertNotNull(savedPlatform1);
        assertNotNull(savedPlatform2);

        var game1 = new GameEntity(null, "game#1", savedDeveloper.id());
        var game2 = new GameEntity(null, "game#2", savedDeveloper.id());

        var savedGame1 = gameRepository.save(game1).block();
        var savedGame2 = gameRepository.save(game2).block();

        assertNotNull(savedGame1);
        assertNotNull(savedGame2);

        var gameGenre1 = new GameGenreEntity(savedGame1.id(), savedGenre1.id());
        var gameGenre2 = new GameGenreEntity(savedGame2.id(), savedGenre2.id());
        var gamePlatform1 = new GamePlatformEntity(savedGame1.id(), savedPlatform1.id());
        var gamePlatform2 = new GamePlatformEntity(savedGame2.id(), savedPlatform2.id());

        gameGenreRepository.saveAll(List.of(gameGenre1, gameGenre2)).blockLast();
        gamePlatformRepository.saveAll(List.of(gamePlatform1, gamePlatform2)).blockLast();

        webClient.get()
                .uri("/api/v1/games")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0].id").isEqualTo(savedGame1.id())
                .jsonPath("$[0].name").isEqualTo("game#1")
                .jsonPath("$[0].developer.id").isEqualTo(savedDeveloper.id())
                .jsonPath("$[0].genres").doesNotExist()
                .jsonPath("$[0].platforms").doesNotExist()
                .jsonPath("$[1].id").isEqualTo(savedGame2.id())
                .jsonPath("$[1].name").isEqualTo("game#2")
                .jsonPath("$[1].developer.id").isEqualTo(savedDeveloper.id())
                .jsonPath("$[1].genres").doesNotExist()
                .jsonPath("$[1].platforms").doesNotExist();
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoEntities() {
        webClient.get()
                .uri("/api/v1/games")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GameDto.class)
                .hasSize(0);
    }

    @Test
    void update_ShouldReturnUpdatedEntity_WhenFound() {
        var developer1 = new DeveloperEntity(null, "developer#1", "description#1");
        var developer2 = new DeveloperEntity(null, "developer#2", "description#2");
        var genre1 = new GenreEntity(null, "genre#1");
        var genre2 = new GenreEntity(null, "genre#2");
        var platform1 = new PlatformEntity(null, "platform#1", "manufacturer#1");
        var platform2 = new PlatformEntity(null, "platform#2", "manufacturer#2");

        var savedDeveloper1 = developerRepository.save(developer1).block();
        var savedDeveloper2 = developerRepository.save(developer2).block();
        var savedGenre1 = genreRepository.save(genre1).block();
        var savedGenre2 = genreRepository.save(genre2).block();
        var savedPlatform1 = platformRepository.save(platform1).block();
        var savedPlatform2 = platformRepository.save(platform2).block();

        assertNotNull(savedDeveloper1);
        assertNotNull(savedDeveloper2);
        assertNotNull(savedGenre1);
        assertNotNull(savedGenre2);
        assertNotNull(savedPlatform1);
        assertNotNull(savedPlatform2);

        var game = new GameEntity(null, "game#1", savedDeveloper1.id());
        var savedGame = gameRepository.save(game).block();

        assertNotNull(savedGame);

        var gameGenre = new GameGenreEntity(savedGame.id(), savedGenre1.id());
        var gamePlatform = new GamePlatformEntity(savedGame.id(), savedPlatform1.id());

        gameGenreRepository.save(gameGenre).block();
        gamePlatformRepository.save(gamePlatform).block();

        var updatedGame = new GameDto(
                savedGame.id(),
                "game#2",
                new DeveloperDto(savedDeveloper2.id(), null, null),
                List.of(new GenreDto(savedGenre2.id(), null)),
                List.of(new PlatformDto(savedPlatform2.id(), null, null))
        );

        webClient.put()
                .uri("/api/v1/games/{id}", savedGame.id())
                .bodyValue(updatedGame)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedGame.id())
                .jsonPath("$.name").isEqualTo("game#2")
                .jsonPath("$.developer.id").isEqualTo(savedDeveloper2.id())
                .jsonPath("$.genres[0].id").isEqualTo(savedGenre2.id())
                .jsonPath("$.platforms[0].id").isEqualTo(savedPlatform2.id());
    }

    @Test
    void update_ShouldReturnNotFound_WhenEntityMissing() {
        var developer = new DeveloperEntity(null, "developer#1", "description#1");
        var genre = new GenreEntity(null, "genre#1");
        var platform = new PlatformEntity(null, "platform#1", "manufacturer#1");

        var savedDeveloper = developerRepository.save(developer).block();
        var savedGenre = genreRepository.save(genre).block();
        var savedPlatform = platformRepository.save(platform).block();

        assertNotNull(savedDeveloper);
        assertNotNull(savedGenre);
        assertNotNull(savedPlatform);

        var game = new GameDto(
                UUID.randomUUID(),
                "game#1",
                new DeveloperDto(savedDeveloper.id(), null, null),
                List.of(new GenreDto(savedGenre.id(), null)),
                List.of(new PlatformDto(savedPlatform.id(), null, null))
        );

        webClient.put()
                .uri("/api/v1/games/{id}", game.id())
                .bodyValue(game)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void partialUpdate_ShouldReturnPatchedEntity_WhenFound() {
        var developer = new DeveloperEntity(null, "developer#1", "description#1");
        var genre = new GenreEntity(null, "genre#1");
        var platform = new PlatformEntity(null, "platform#1", "manufacturer#1");

        var savedDeveloper = developerRepository.save(developer).block();
        var savedGenre = genreRepository.save(genre).block();
        var savedPlatform = platformRepository.save(platform).block();

        assertNotNull(savedDeveloper);
        assertNotNull(savedGenre);
        assertNotNull(savedPlatform);

        var game = new GameEntity(null, "game#1", savedDeveloper.id());
        var savedGame = gameRepository.save(game).block();

        assertNotNull(savedGame);

        var gameGenre = new GameGenreEntity(savedGame.id(), savedGenre.id());
        var gamePlatform = new GamePlatformEntity(savedGame.id(), savedPlatform.id());

        gameGenreRepository.save(gameGenre).block();
        gamePlatformRepository.save(gamePlatform).block();

        var partialUpdate = new GameDto(
                savedGame.id(),
                "game#2",
                null,
                null,
                null
        );

        webClient.patch()
                .uri("/api/v1/games/{id}", savedGame.id())
                .bodyValue(partialUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedGame.id())
                .jsonPath("$.name").isEqualTo("game#2")
                .jsonPath("$.developer.id").isEqualTo(savedDeveloper.id())
                .jsonPath("$.genres[0].id").isEqualTo(savedGenre.id())
                .jsonPath("$.platforms[0].id").isEqualTo(savedPlatform.id());
    }

    @Test
    void partialUpdate_ShouldReturnNotFound_WhenEntityMissing() {
        var partialUpdate = new GameDto(
                UUID.randomUUID(),
                "game#2",
                null,
                null,
                null
        );

        webClient.patch()
                .uri("/api/v1/games/{id}", UUID.randomUUID())
                .bodyValue(partialUpdate)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void delete_ShouldReturnNoContent_WhenDeleted() {
        var developer = new DeveloperEntity(null, "developer#1", "description#1");
        var genre = new GenreEntity(null, "genre#1");
        var platform = new PlatformEntity(null, "platform#1", "manufacturer#1");

        var savedDeveloper = developerRepository.save(developer).block();
        var savedGenre = genreRepository.save(genre).block();
        var savedPlatform = platformRepository.save(platform).block();

        assertNotNull(savedDeveloper);
        assertNotNull(savedGenre);
        assertNotNull(savedPlatform);

        var game = new GameEntity(null, "game#1", savedDeveloper.id());
        var savedGame = gameRepository.save(game).block();

        assertNotNull(savedGame);

        var gameGenre = new GameGenreEntity(savedGame.id(), savedGenre.id());
        var gamePlatform = new GamePlatformEntity(savedGame.id(), savedPlatform.id());

        gameGenreRepository.save(gameGenre).block();
        gamePlatformRepository.save(gamePlatform).block();

        webClient.delete()
                .uri("/api/v1/games/{id}", savedGame.id())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void delete_ShouldReturnNotFound_WhenEntityMissing() {
        webClient.delete()
                .uri("/api/v1/games/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNoContent();
    }
}