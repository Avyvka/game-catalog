package com.github.avyvka.game.library.controller.api;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveCrudController<D, ID> {

    @PostMapping
    Mono<D> create(@Validated @RequestBody Mono<D> dto);

    @GetMapping("/{id}")
    Mono<D> getById(@PathVariable ID id);

    @GetMapping
    Flux<D> getAll(@PageableDefault(size = 20) Pageable pageable);

    @PutMapping("/{id}")
    Mono<D> update(@PathVariable ID id, @Validated @RequestBody Mono<D> dto);

    @PatchMapping("/{id}")
    Mono<D> partialUpdate(@PathVariable ID id, @Validated @RequestBody Mono<D> dto);

    @DeleteMapping("/{id}")
    Mono<Void> delete(@PathVariable ID id);
}