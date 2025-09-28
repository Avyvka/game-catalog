package com.github.avyvka.game.catalog.service;

import com.github.avyvka.game.catalog.model.dto.GenreDto;
import com.github.avyvka.game.catalog.service.api.ReactiveCrudService;

import java.util.UUID;

public interface GenreService extends ReactiveCrudService<GenreDto, UUID> {
}
