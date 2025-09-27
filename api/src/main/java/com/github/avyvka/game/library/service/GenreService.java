package com.github.avyvka.game.library.service;

import com.github.avyvka.game.library.mapper.api.EntityDtoMapper;
import com.github.avyvka.game.library.model.dto.GenreDto;
import com.github.avyvka.game.library.model.entity.GenreEntity;
import com.github.avyvka.game.library.repository.api.CustomR2dbcRepository;
import com.github.avyvka.game.library.service.support.AbstractReactiveCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GenreService extends AbstractReactiveCrudService<GenreEntity, GenreDto, UUID> {

    @Autowired
    protected GenreService(
            CustomR2dbcRepository<GenreEntity, UUID> repository,
            EntityDtoMapper<GenreEntity, GenreDto> mapper
    ) {
        super(repository, mapper);
    }
}
