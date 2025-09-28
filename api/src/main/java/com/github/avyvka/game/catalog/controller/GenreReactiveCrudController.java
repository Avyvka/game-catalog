package com.github.avyvka.game.catalog.controller;

import com.github.avyvka.game.catalog.controller.support.AbstractReactiveCrudController;
import com.github.avyvka.game.catalog.model.dto.GenreDto;
import com.github.avyvka.game.catalog.service.GenreService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/genres")
public class GenreReactiveCrudController extends AbstractReactiveCrudController<GenreDto, UUID> {

    protected GenreReactiveCrudController(GenreService service) {
        super(service);
    }
}
