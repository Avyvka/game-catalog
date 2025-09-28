package com.github.avyvka.game.library.service.impl;

import com.github.avyvka.game.library.mapper.api.EntityDtoMapper;
import com.github.avyvka.game.library.model.dto.GenreDto;
import com.github.avyvka.game.library.model.entity.GenreEntity;
import com.github.avyvka.game.library.repository.GenreRepository;
import com.github.avyvka.game.library.service.GenreService;
import com.github.avyvka.game.library.service.support.AbstractReactiveCrudService;
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
