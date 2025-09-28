package com.github.avyvka.game.catalog.controller.api;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveCrudController<D, ID> {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<D> create(@Valid @RequestBody Mono<D> dto);

    @GetMapping("/{id}")
    Mono<D> getById(@PathVariable ID id);

    @GetMapping({"", "/"})
    Flux<D> getAll(@PageableDefault(size = 20) Pageable pageable);

    @PutMapping("/{id}")
    Mono<D> update(@PathVariable ID id, @Valid @RequestBody Mono<D> dto);

    @PatchMapping("/{id}")
    Mono<D> partialUpdate(@PathVariable ID id, @RequestBody Mono<D> dto);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<Void> delete(@PathVariable ID id);
}