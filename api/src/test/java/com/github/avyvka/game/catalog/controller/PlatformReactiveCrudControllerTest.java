package com.github.avyvka.game.catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.avyvka.game.catalog.model.dto.PlatformDto;
import com.github.avyvka.game.catalog.model.entity.PlatformEntity;
import com.github.avyvka.game.catalog.repository.PlatformRepository;
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
class PlatformReactiveCrudControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlatformRepository platformRepository;

    @BeforeEach
    void setup() {
        platformRepository.deleteAll().block();
    }

    @Test
    void create_ShouldReturnCreatedEntity() throws IOException {
        var platform = new PlatformDto(null, "platform#1", "manufacturer#1");

        var response = webClient.post()
                .uri("/api/v1/platforms")
                .bodyValue(platform)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.name").isEqualTo("platform#1")
                .jsonPath("$.manufacturer").isEqualTo("manufacturer#1")
                .returnResult()
                .getResponseBody();

        var created = objectMapper.readValue(response, PlatformDto.class);

        var saved = platformRepository.findById(created.id()).block();

        assertNotNull(saved);
        assertThat(saved.name()).isEqualTo("platform#1");
        assertThat(saved.manufacturer()).isEqualTo("manufacturer#1");
    }


    @Test
    void getById_ShouldReturnEntity_WhenFound() {
        var platform = new PlatformEntity(null, "platform#1", "manufacturer#1");

        var saved = platformRepository.save(platform).block();

        assertNotNull(saved);

        webClient.get()
                .uri("/api/v1/platforms/{id}", saved.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(saved.id())
                .jsonPath("$.name").isEqualTo("platform#1")
                .jsonPath("$.manufacturer").isEqualTo("manufacturer#1");
    }

    @Test
    void getById_ShouldReturnNotFound_WhenEmpty() {
        webClient.get()
                .uri("/api/v1/platforms/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAll_ShouldReturnList() {
        var platform1 = new PlatformEntity(null, "platform#1", "manufacturer#1");
        var platform2 = new PlatformEntity(null, "platform#2", "manufacturer#2");

        platformRepository.saveAll(List.of(platform1, platform2)).blockLast();

        webClient.get()
                .uri("/api/v1/platforms")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PlatformDto.class)
                .hasSize(2)
                .value(list -> {
                    assertThat(list).extracting(PlatformDto::id)
                            .isNotNull();
                    assertThat(list).extracting(PlatformDto::name)
                            .contains("platform#1", "platform#2");
                    assertThat(list).extracting(PlatformDto::manufacturer)
                            .contains("manufacturer#1", "manufacturer#2");
                });
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoEntities() {
        webClient.get()
                .uri("/api/v1/platforms")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PlatformDto.class)
                .hasSize(0);
    }

    @Test
    void update_ShouldReturnUpdatedEntity_WhenFound() {
        var platform1 = new PlatformEntity(null, "platform#1", "manufacturer#1");
        var platform2 = new PlatformEntity(UUID.randomUUID(), "platform#2", "manufacturer#2");

        var saved = platformRepository.save(platform1).block();

        assertNotNull(saved);

        webClient.put()
                .uri("/api/v1/platforms/{id}", saved.id())
                .bodyValue(platform2)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(saved.id())
                .jsonPath("$.name").isEqualTo("platform#2")
                .jsonPath("$.manufacturer").isEqualTo("manufacturer#2");
    }

    @Test
    void update_ShouldReturnNotFound_WhenEntityMissing() {
        var platform = new PlatformEntity(UUID.randomUUID(), "platform#1", "manufacturer#1");

        webClient.put()
                .uri("/api/v1/platforms/{id}", UUID.randomUUID())
                .bodyValue(platform)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void partialUpdate_ShouldReturnPatchedEntity_WhenFound() {
        var platform1 = new PlatformEntity(null, "platform#1", "manufacturer#1");
        var platform2 = new PlatformEntity(null, "platform#2", null);

        var saved = platformRepository.save(platform1).block();

        assertNotNull(saved);

        webClient.patch()
                .uri("/api/v1/platforms/{id}", saved.id())
                .bodyValue(platform2)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(saved.id())
                .jsonPath("$.name").isEqualTo("platform#2")
                .jsonPath("$.manufacturer").isEqualTo("manufacturer#1");
    }

    @Test
    void partialUpdate_ShouldReturnNotFound_WhenEntityMissing() {
        var platform = new PlatformEntity(UUID.randomUUID(), "platform#1", "manufacturer#1");

        webClient.patch()
                .uri("/api/v1/platforms/{id}", UUID.randomUUID())
                .bodyValue(platform)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void delete_ShouldReturnNoContent_WhenDeleted() {
        var platform = new PlatformEntity(null, "platform#1", "manufacturer#1");

        var saved = platformRepository.save(platform).block();

        assertNotNull(saved);

        webClient.delete()
                .uri("/api/v1/platforms/{id}", saved.id())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void delete_ShouldReturnNotFound_WhenEntityMissing() {
        webClient.delete()
                .uri("/api/v1/platforms/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isNoContent();
    }
}