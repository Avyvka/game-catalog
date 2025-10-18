package com.github.avyvka.game.catalog.service.api;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveCrudService<D, ID> {

    Mono<D> create(Mono<D> dto);

    Mono<D> findById(ID id);

    Flux<D> findAll(Pageable pageable);

    Mono<D> update(ID id, Mono<D> dto);

    Mono<D> partialUpdate(ID id, Mono<D> dto);

    Mono<Void> delete(ID id);

    Mono<Long> count();
}