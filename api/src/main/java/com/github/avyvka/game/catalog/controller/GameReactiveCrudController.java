package com.github.avyvka.game.catalog.controller;

import com.github.avyvka.game.catalog.controller.support.AbstractReactiveCrudController;
import com.github.avyvka.game.catalog.model.dto.GameDto;
import com.github.avyvka.game.catalog.service.GameService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/games")
public class GameReactiveCrudController extends AbstractReactiveCrudController<GameDto, UUID> {

    protected GameReactiveCrudController(GameService service) {
        super(service);
    }
}
