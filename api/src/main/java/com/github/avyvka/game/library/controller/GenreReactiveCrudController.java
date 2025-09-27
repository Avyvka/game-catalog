package com.github.avyvka.game.library.controller;

import com.github.avyvka.game.library.controller.support.AbstractReactiveCrudController;
import com.github.avyvka.game.library.model.dto.GenreDto;
import com.github.avyvka.game.library.service.api.ReactiveCrudService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/genres")
public class GenreReactiveCrudController extends AbstractReactiveCrudController<GenreDto, UUID> {

    protected GenreReactiveCrudController(ReactiveCrudService<GenreDto, UUID> service) {
        super(service);
    }
}
