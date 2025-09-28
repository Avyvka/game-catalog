package com.github.avyvka.game.library.service;

import com.github.avyvka.game.library.model.dto.DeveloperDto;
import com.github.avyvka.game.library.service.api.ReactiveCrudService;

import java.util.UUID;

public interface DeveloperService extends ReactiveCrudService<DeveloperDto, UUID> {
}
