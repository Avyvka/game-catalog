package com.github.avyvka.game.library.controller.support;

import com.github.avyvka.game.library.config.WebConfig;
import com.github.avyvka.game.library.service.api.ReactiveCrudService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest
@Import({WebConfig.class})
class AbstractReactiveCrudControllerTest {

    record TestDto(UUID id) {}

    private final UUID id = UUID.randomUUID();

    private final TestDto testDto = new TestDto(id);

    @Configuration
    static class TestControllerConfiguration {

        @RestController
        @RequestMapping("/api/v1/test")
        static class TestController extends AbstractReactiveCrudController<TestDto, UUID> {

            @Autowired
            protected TestController(ReactiveCrudService<TestDto, UUID> service) {
                super(service);
            }
        }
    }

    @Autowired
    private WebTestClient webClient;

    @MockitoBean
    private ReactiveCrudService<TestDto, UUID> service;

    //

    @Test
    void create_ShouldReturnCreatedEntity() {
        when(service.create(any())).thenReturn(Mono.just(testDto));

        webClient.post()
                .uri("/api/v1/test")
                .bodyValue(testDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TestDto.class)
                .isEqualTo(testDto);
    }

    @Test
    void getById_ShouldReturnEntity_WhenFound() {
        when(service.findById(id)).thenReturn(Mono.just(testDto));

        webClient.get()
                .uri("/api/v1/test/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TestDto.class)
                .isEqualTo(testDto);
    }

    @Test
    void getById_ShouldReturnNotFound_WhenEmpty() {
        when(service.findById(id)).thenReturn(Mono.empty());

        webClient.get()
                .uri("/api/v1/test/{id}", id)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAll_ShouldReturnList() {
        when(service.findAll(any())).thenReturn(Flux.just(testDto));

        webClient.get()
                .uri("/api/v1/test")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TestDto.class)
                .contains(testDto);
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoEntities() {
        when(service.findAll(any())).thenReturn(Flux.empty());

        webClient.get()
                .uri("/api/v1/test")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TestDto.class)
                .hasSize(0);
    }

    @Test
    void update_ShouldReturnUpdatedEntity_WhenFound() {
        when(service.update(eq(id), any())).thenReturn(Mono.just(testDto));

        webClient.put()
                .uri("/api/v1/test/{id}", id)
                .bodyValue(testDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TestDto.class)
                .isEqualTo(testDto);
    }

    @Test
    void update_ShouldReturnNotFound_WhenEntityMissing() {
        when(service.update(eq(id), any())).thenReturn(Mono.empty());

        webClient.put()
                .uri("/api/v1/test/{id}", id)
                .bodyValue(testDto)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void partialUpdate_ShouldReturnPatchedEntity_WhenFound() {
        when(service.partialUpdate(eq(id), any())).thenReturn(Mono.just(testDto));

        webClient.patch()
                .uri("/api/v1/test/{id}", id)
                .bodyValue(testDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TestDto.class)
                .isEqualTo(testDto);
    }

    @Test
    void partialUpdate_ShouldReturnNotFound_WhenEntityMissing() {
        when(service.partialUpdate(eq(id), any())).thenReturn(Mono.empty());

        webClient.patch()
                .uri("/api/v1/test/{id}", id)
                .bodyValue(testDto)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void delete_ShouldReturnNoContent_WhenDeleted() {
        when(service.delete(id)).thenReturn(Mono.empty());

        webClient.delete()
                .uri("/api/v1/test/{id}", id)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void delete_ShouldReturnNotFound_WhenEntityMissing() {
        when(service.delete(id)).thenReturn(Mono.empty());

        webClient.delete()
                .uri("/api/v1/test/{id}", id)
                .exchange()
                .expectStatus().isNoContent();
    }
}