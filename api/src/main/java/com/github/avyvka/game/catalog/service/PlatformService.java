package com.github.avyvka.game.catalog.service;

import com.github.avyvka.game.catalog.model.dto.PlatformDto;
import com.github.avyvka.game.catalog.service.api.ReactiveCrudService;

import java.util.UUID;

public interface PlatformService extends ReactiveCrudService<PlatformDto, UUID> {
}
