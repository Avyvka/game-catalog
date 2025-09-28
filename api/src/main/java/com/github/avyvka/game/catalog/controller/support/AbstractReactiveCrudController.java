package com.github.avyvka.game.catalog.controller.support;

import com.github.avyvka.game.catalog.controller.api.ReactiveCrudController;
import com.github.avyvka.game.catalog.service.api.ReactiveCrudService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<D> create(@Valid @RequestBody Mono<D> dto) {
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

    @GetMapping({"", "/"})
    public Flux<D> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(pageable);
    }

    @PutMapping("/{id}")
    public Mono<D> update(@PathVariable ID id, @Valid @RequestBody Mono<D> dto) {
        return service.update(id, dto)
                .switchIfEmpty(
                        Mono.error(
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND, "Entity with ID " + id + " not found."
                                )
                        )
                );
    }

    @PatchMapping("/{id}")
    public Mono<D> partialUpdate(@PathVariable ID id, @Valid @RequestBody Mono<D> dto) {
        return service.partialUpdate(id, dto)
                .switchIfEmpty(
                        Mono.error(
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND, "Entity with ID " + id + " not found."
                                )
                        )
                );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable ID id) {
        return service.delete(id);
    }
}