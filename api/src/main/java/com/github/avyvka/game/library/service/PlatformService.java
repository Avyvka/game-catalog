package com.github.avyvka.game.library.service;

import com.github.avyvka.game.library.model.dto.PlatformDto;
import com.github.avyvka.game.library.service.api.ReactiveCrudService;

import java.util.UUID;

public interface PlatformService extends ReactiveCrudService<PlatformDto, UUID> {
}
