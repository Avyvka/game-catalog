package com.github.avyvka.game.library.service.support;

import com.github.avyvka.game.library.mapper.EntityDtoMapper;
import com.github.avyvka.game.library.repository.api.CustomR2dbcRepository;
import com.github.avyvka.game.library.service.api.ReactiveCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class AbstractReactiveCrudService<E, D, ID> implements ReactiveCrudService<D, ID> {

    private final CustomR2dbcRepository<E, ID> repository;

    private final EntityDtoMapper<E, D> mapper;

    @Autowired
    protected AbstractReactiveCrudService(
            CustomR2dbcRepository<E, ID> repository,
            EntityDtoMapper<E, D> mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<D> create(Mono<D> dto) {
        return dto
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    @Override
    public Mono<D> findById(ID id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public Flux<D> findAll(Pageable pageable) {
        return repository.findAllBy(pageable).map(mapper::toDto);
    }

    @Override
    public Mono<D> update(ID id, Mono<D> dtoMono) {
        return repository.findById(id)
                .zipWith(dtoMono, mapper::update)
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    @Override
    public Mono<D> partialUpdate(ID id, Mono<D> dtoMono) {
        return repository.findById(id)
                .zipWith(dtoMono, mapper::partialUpdate)
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> delete(ID id) {
        return repository.deleteById(id);
    }
}