package com.github.avyvka.game.catalog.controller;

import com.github.avyvka.game.catalog.controller.support.AbstractReactiveCrudController;
import com.github.avyvka.game.catalog.model.dto.DeveloperDto;
import com.github.avyvka.game.catalog.service.DeveloperService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/developers")
public class DeveloperReactiveCrudController extends AbstractReactiveCrudController<DeveloperDto, UUID> {

    protected DeveloperReactiveCrudController(DeveloperService service) {
        super(service);
    }
}
