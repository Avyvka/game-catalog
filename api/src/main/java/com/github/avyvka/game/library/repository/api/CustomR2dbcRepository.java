package com.github.avyvka.game.library.repository.api;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;

@NoRepositoryBean
public interface CustomR2dbcRepository<E, ID> extends R2dbcRepository<E, ID> {

    Flux<E> findAllBy(Pageable pageable);
}
