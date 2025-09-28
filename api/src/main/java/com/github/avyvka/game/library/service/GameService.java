package com.github.avyvka.game.library.service;

import com.github.avyvka.game.library.model.dto.GameDto;
import com.github.avyvka.game.library.service.api.ReactiveCrudService;

import java.util.UUID;

public interface GameService extends ReactiveCrudService<GameDto, UUID> {
}
