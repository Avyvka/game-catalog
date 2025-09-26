package com.github.avyvka.game.library.service.support;

import com.github.avyvka.game.library.mapper.EntityDtoMapper;
import com.github.avyvka.game.library.repository.api.CustomR2dbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractReactiveCrudServiceTest {

    record TestDto(UUID id) {}

    record TestEntity(UUID id) {}
    
    @Mock
    private CustomR2dbcRepository<TestEntity, UUID> repository;

    @Mock
    private EntityDtoMapper<TestEntity, TestDto> mapper;

    private AbstractReactiveCrudService<TestEntity, TestDto, UUID> service;

    private final UUID testId = UUID.randomUUID();
    
    private final TestDto testDto = new TestDto(testId);

    private final TestEntity testEntity = new TestEntity(testId);

    @BeforeEach
    void setUp() {
        service = new AbstractReactiveCrudService<>(repository, mapper) {};
    }
    
    @Test
    void create_ShouldSaveAndReturnDto() {
        when(mapper.toEntity(testDto)).thenReturn(testEntity);
        when(repository.save(testEntity)).thenReturn(Mono.just(testEntity));
        when(mapper.toDto(testEntity)).thenReturn(testDto);

        StepVerifier.create(service.create(Mono.just(testDto)))
                .expectNext(testDto)
                .verifyComplete();
    }

    @Test
    void findById_ShouldReturnDto_WhenFound() {
        when(repository.findById(testId)).thenReturn(Mono.just(testEntity));
        when(mapper.toDto(testEntity)).thenReturn(testDto);

        StepVerifier.create(service.findById(testId))
                .expectNext(testDto)
                .verifyComplete();
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotFound() {
        when(repository.findById(testId)).thenReturn(Mono.empty());

        StepVerifier.create(service.findById(testId))
                .verifyComplete();
    }

    @Test
    void findAll_ShouldReturnDtoFlux() {
        when(repository.findAll(Pageable.unpaged())).thenReturn(Flux.just(testEntity));
        when(mapper.toDto(testEntity)).thenReturn(testDto);

        StepVerifier.create(service.findAll(Pageable.unpaged()))
                .expectNext(testDto)
                .verifyComplete();
    }

    @Test
    void findAll_ShouldReturnEmptyFlux_WhenNoEntities() {
        when(repository.findAll(Pageable.unpaged())).thenReturn(Flux.empty());

        StepVerifier.create(service.findAll(Pageable.unpaged()))
                .verifyComplete();
    }

    @Test
    void update_ShouldUpdateAndReturnDto_WhenEntityExists() {
        when(repository.findById(testId)).thenReturn(Mono.just(testEntity));
        when(mapper.update(testEntity, testDto)).thenReturn(testEntity);
        when(repository.save(testEntity)).thenReturn(Mono.just(testEntity));
        when(mapper.toDto(testEntity)).thenReturn(testDto);

        StepVerifier.create(service.update(testId, Mono.just(testDto)))
                .expectNext(testDto)
                .verifyComplete();
    }

    @Test
    void update_ShouldReturnEmpty_WhenEntityMissing() {
        when(repository.findById(testId)).thenReturn(Mono.empty());

        StepVerifier.create(service.update(testId, Mono.just(testDto)))
                .verifyComplete();
    }

    @Test
    void partialUpdate_ShouldUpdateAndReturnDto_WhenEntityExists() {
        when(repository.findById(testId)).thenReturn(Mono.just(testEntity));
        when(mapper.partialUpdate(testEntity, testDto)).thenReturn(testEntity);
        when(repository.save(testEntity)).thenReturn(Mono.just(testEntity));
        when(mapper.toDto(testEntity)).thenReturn(testDto);

        StepVerifier.create(service.partialUpdate(testId, Mono.just(testDto)))
                .expectNext(testDto)
                .verifyComplete();
    }

    @Test
    void partialUpdate_ShouldReturnEmpty_WhenEntityMissing() {
        when(repository.findById(testId)).thenReturn(Mono.empty());

        StepVerifier.create(service.partialUpdate(testId, Mono.just(testDto)))
                .verifyComplete();
    }

    @Test
    void delete_ShouldComplete_WhenEntityDeleted() {
        when(repository.deleteById(testId)).thenReturn(Mono.empty());

        StepVerifier.create(service.delete(testId))
                .verifyComplete();
    }
}