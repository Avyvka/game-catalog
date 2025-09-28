package com.github.avyvka.game.catalog.service;

import com.github.avyvka.game.catalog.model.dto.GameDto;
import com.github.avyvka.game.catalog.service.api.ReactiveCrudService;

import java.util.UUID;

public interface GameService extends ReactiveCrudService<GameDto, UUID> {
}
