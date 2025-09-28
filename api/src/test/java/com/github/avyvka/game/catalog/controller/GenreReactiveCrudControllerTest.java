package com.github.avyvka.game.catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.avyvka.game.catalog.model.dto.GenreDto;
import com.github.avyvka.game.catalog.model.entity.GenreEntity;
import com.github.avyvka.game.catalog.repository.GenreRepository;
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
class GenreReactiveCrudControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void setup() {
        genreRepository.deleteAll().block();
    }

    @Test
    void create_ShouldReturnCreatedEntity() throws IOException {
        var genre = new GenreDto(null, "genre#1");

        var response = webClient.post()
                .uri("/api/v1/genres")
                .bodyValue(genre)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.name").isEqualTo("genre#1")
                .returnResult()
                .getResponseBody();

        var created = objectMapper.readValue(response, GenreDto.class);

        var saved = genreRepository.findById(created.id()).block();

        assertNotNull(saved);
        assertThat(saved.name()).isEqualTo("genre#1");
    }


    @Test
    void getById_ShouldReturnEntity_WhenFound() {
        var genre = new GenreEntity(null, "genre#1");

        var saved = genreRepository.save(genre).block();

        assertNotNull(saved);

        webClient.get()
                .uri("/api/v1/genres/{id}", saved.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(saved.id())
                .jsonPath("$.name").isEqualTo("genre#1");
    }

    @Test
    void getById_ShouldReturnNotFound_WhenEmpty() {
        webClient.get()
                .uri("/api/v1/genres/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAll_ShouldReturnList() {
        var genre1 = new GenreEntity(null, "genre#1");
        var genre2 = new GenreEntity(null, "genre#2");

        genreRepository.saveAll(List.of(genre1, genre2)).blockLast();

        webClient.get()
                .uri("/api/v1/genres")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(2)
                .value(list -> {
                    assertThat(list).extracting(GenreDto::id)
                            .isNotNull();
                    assertThat(list).extracting(GenreDto::name)
                            .contains("genre#1", "genre#2");
                });
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoEntities() {
        webClient.get()
                .uri("/api/v1/genres")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(0);
    }

    @Test
    void update_ShouldReturnUpdatedEntity_WhenFound() {
        var genre1 = new GenreEntity(null, "genre#1");
        var genre2 = new GenreEntity(UUID.randomUUID(), "genre#2");

        var saved = genreRepository.save(genre1).block();

        assertNotNull(saved);

        webClient.put()
                .uri("/api/v1/genres/{id}", saved.id())
                .bodyValue(genre2)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(saved.id())
                .jsonPath("$.name").isEqualTo("genre#2");
    }

    @Test
    void update_ShouldReturnNotFound_WhenEntityMissing() {
        var genre = new GenreEntity(UUID.randomUUID(), "genre#1");

        webClient.put()
                .uri("/api/v1/genres/{id}", UUID.randomUUID())
                .bodyValue(genre)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void partialUpdate_ShouldReturnPatchedEntity_WhenFound() {
        var genre1 = new GenreEntity(null, "genre#1");
        var genre2 = new GenreEntity(null, "genre#2");

        var saved = genreRepository.save(genre1).block();

        assertNotNull(saved);

        webClient.patch()
                .uri("/api/v1/genres/{id}", saved.id())
                .bodyValue(genre2)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(saved.id())
                .jsonPath("$.name").isEqualTo("genre#2");
    }

    @Test
    void partialUpdate_ShouldReturnNotFound_WhenEntityMissing() {
        var genre = new GenreEntity(UUID.randomUUID(), "genre#1");

        webClient.patch()
                .uri("/api/v1/genres/{id}", UUID.randomUUID())
                .bodyValue(genre)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void delete_ShouldReturnNoContent_WhenDeleted() {
        var genre = new GenreEntity(null, "genre#1");

        var saved = genreRepository.save(genre).block();

        assertNotNull(saved);

        webClient.delete()
                .uri("/api/v1/genres/{id}", saved.id())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void delete_ShouldReturnNotFound_WhenEntityMissing() {
        webClient.delete()
                .uri("/api/v1/genres/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNoContent();
    }
}