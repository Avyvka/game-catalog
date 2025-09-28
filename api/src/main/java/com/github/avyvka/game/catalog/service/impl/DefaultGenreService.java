package com.github.avyvka.game.catalog.service.impl;

import com.github.avyvka.game.catalog.mapper.api.EntityDtoMapper;
import com.github.avyvka.game.catalog.model.dto.GenreDto;
import com.github.avyvka.game.catalog.model.entity.GenreEntity;
import com.github.avyvka.game.catalog.repository.GenreRepository;
import com.github.avyvka.game.catalog.service.GenreService;
import com.github.avyvka.game.catalog.service.support.AbstractReactiveCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DefaultGenreService
        extends AbstractReactiveCrudService<GenreEntity, GenreDto, UUID>
        implements GenreService
{
    @Autowired
    protected DefaultGenreService(
            GenreRepository repository,
            EntityDtoMapper<GenreEntity, GenreDto> mapper
    ) {
        super(repository, mapper);
    }
}
