package com.github.avyvka.game.catalog.service.support;

import com.github.avyvka.game.catalog.mapper.api.EntityDtoMapper;
import com.github.avyvka.game.catalog.repository.api.CustomR2dbcRepository;
import com.github.avyvka.game.catalog.service.api.ReactiveCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Mono<D> update(ID id, Mono<D> dtoMono) {
        return Mono.zip(repository.findById(id), dtoMono)
                .map(tuple -> mapper.update(tuple.getT1(), tuple.getT2()))
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    @Override
    public Mono<D> partialUpdate(ID id, Mono<D> dtoMono) {
        return Mono.zip(repository.findById(id), dtoMono)
                .map(tuple -> mapper.partialUpdate(tuple.getT1(), tuple.getT2()))
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> delete(ID id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Long> count() {
        return repository.count();
    }
}