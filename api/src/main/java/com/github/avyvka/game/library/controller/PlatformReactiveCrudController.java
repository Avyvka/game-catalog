package com.github.avyvka.game.library.controller;

import com.github.avyvka.game.library.controller.support.AbstractReactiveCrudController;
import com.github.avyvka.game.library.model.dto.PlatformDto;
import com.github.avyvka.game.library.service.PlatformService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/platforms")
public class PlatformReactiveCrudController extends AbstractReactiveCrudController<PlatformDto, UUID> {

    protected PlatformReactiveCrudController(PlatformService service) {
        super(service);
    }
}
