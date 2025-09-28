package com.github.avyvka.game.catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.avyvka.game.catalog.model.dto.DeveloperDto;
import com.github.avyvka.game.catalog.model.entity.DeveloperEntity;
import com.github.avyvka.game.catalog.repository.DeveloperRepository;
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
class DeveloperReactiveCrudControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DeveloperRepository developerRepository;

    @BeforeEach
    void setup() {
        developerRepository.deleteAll().block();
    }

    @Test
    void create_ShouldReturnCreatedEntity() throws IOException {
        var developer = new DeveloperDto(null, "developer#1", "description#1");

        var response = webClient.post()
                .uri("/api/v1/developers")
                .bodyValue(developer)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.name").isEqualTo("developer#1")
                .jsonPath("$.description").isEqualTo("description#1")
                .returnResult()
                .getResponseBody();

        var created = objectMapper.readValue(response, DeveloperDto.class);

        var saved = developerRepository.findById(created.id()).block();

        assertNotNull(saved);
        assertThat(saved.name()).isEqualTo("developer#1");
        assertThat(saved.description()).isEqualTo("description#1");
    }


    @Test
    void getById_ShouldReturnEntity_WhenFound() {
        var developer = new DeveloperEntity(null, "developer#1", "description#1");

        var saved = developerRepository.save(developer).block();

        assertNotNull(saved);

        webClient.get()
                .uri("/api/v1/developers/{id}", saved.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(saved.id())
                .jsonPath("$.name").isEqualTo("developer#1")
                .jsonPath("$.description").isEqualTo("description#1");
    }

    @Test
    void getById_ShouldReturnNotFound_WhenEmpty() {
        webClient.get()
                .uri("/api/v1/developers/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAll_ShouldReturnList() {
        var developer1 = new DeveloperEntity(null, "developer#1", "description#1");
        var developer2 = new DeveloperEntity(null, "developer#2", "description#2");

        developerRepository.saveAll(List.of(developer1, developer2)).blockLast();

        webClient.get()
                .uri("/api/v1/developers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DeveloperDto.class)
                .hasSize(2)
                .value(list -> {
                    assertThat(list).extracting(DeveloperDto::id)
                            .isNotNull();
                    assertThat(list).extracting(DeveloperDto::name)
                            .contains("developer#1", "developer#2");
                    assertThat(list).extracting(DeveloperDto::description)
                            .contains("description#1", "description#2");
                });
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoEntities() {
        webClient.get()
                .uri("/api/v1/developers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DeveloperDto.class)
                .hasSize(0);
    }

    @Test
    void update_ShouldReturnUpdatedEntity_WhenFound() {
        var developer1 = new DeveloperEntity(null, "developer#1", "description#1");
        var developer2 = new DeveloperEntity(UUID.randomUUID(), "developer#2", "description#2");

        var saved = developerRepository.save(developer1).block();

        assertNotNull(saved);

        webClient.put()
                .uri("/api/v1/developers/{id}", saved.id())
                .bodyValue(developer2)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(saved.id())
                .jsonPath("$.name").isEqualTo("developer#2")
                .jsonPath("$.description").isEqualTo("description#2");
    }

    @Test
    void update_ShouldReturnNotFound_WhenEntityMissing() {
        var developer = new DeveloperEntity(UUID.randomUUID(), "developer#1", "description#1");

        webClient.put()
                .uri("/api/v1/developers/{id}", UUID.randomUUID())
                .bodyValue(developer)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void partialUpdate_ShouldReturnPatchedEntity_WhenFound() {
        var developer1 = new DeveloperEntity(null, "developer#1", "description#1");
        var developer2 = new DeveloperEntity(null, "developer#2", null);

        var saved = developerRepository.save(developer1).block();

        assertNotNull(saved);

        webClient.patch()
                .uri("/api/v1/developers/{id}", saved.id())
                .bodyValue(developer2)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(saved.id())
                .jsonPath("$.name").isEqualTo("developer#2")
                .jsonPath("$.description").isEqualTo("description#1");
    }

    @Test
    void partialUpdate_ShouldReturnNotFound_WhenEntityMissing() {
        var developer = new DeveloperEntity(UUID.randomUUID(), "developer#1", "description#1");

        webClient.patch()
                .uri("/api/v1/developers/{id}", UUID.randomUUID())
                .bodyValue(developer)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void delete_ShouldReturnNoContent_WhenDeleted() {
        var developer = new DeveloperEntity(null, "developer#1", "description#1");

        var saved = developerRepository.save(developer).block();

        assertNotNull(saved);

        webClient.delete()
                .uri("/api/v1/developers/{id}", saved.id())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void delete_ShouldReturnNotFound_WhenEntityMissing() {
        webClient.delete()
                .uri("/api/v1/developers/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNoContent();
    }
}