package com.github.avyvka.game.catalog.service;

import com.github.avyvka.game.catalog.model.dto.DeveloperDto;
import com.github.avyvka.game.catalog.service.api.ReactiveCrudService;

import java.util.UUID;

public interface DeveloperService extends ReactiveCrudService<DeveloperDto, UUID> {
}
