package com.github.avyvka.game.library.controller.support;

import com.github.avyvka.game.library.controller.api.ReactiveCrudController;
import com.github.avyvka.game.library.service.api.ReactiveCrudService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class AbstractReactiveCrudController<D, ID> implements ReactiveCrudController<D, ID> {

    private final ReactiveCrudService<D, ID> service;

    protected AbstractReactiveCrudController(ReactiveCrudService<D, ID> service) {
        this.service = service;
    }

    @PostMapping
    public Mono<D> create(@Validated @RequestBody Mono<D> dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public Mono<D> getById(@PathVariable ID id) {
        return service.findById(id)
                .switchIfEmpty(
                        Mono.error(
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND, "Entity with ID " + id + " not found."
                                )
                        )
                );
    }

    @GetMapping
    public Flux<D> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(pageable);
    }

    @PutMapping("/{id}")
    public Mono<D> update(@PathVariable ID id, @Validated @RequestBody Mono<D> dto) {
        return service.update(id, dto);
    }

    @PatchMapping("/{id}")
    public Mono<D> partialUpdate(@PathVariable ID id, @Validated @RequestBody Mono<D> dto) {
        return service.partialUpdate(id, dto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable ID id) {
        return service.delete(id);
    }
}