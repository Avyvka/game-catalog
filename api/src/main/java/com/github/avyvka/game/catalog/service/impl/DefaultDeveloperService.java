package com.github.avyvka.game.catalog.service.impl;

import com.github.avyvka.game.catalog.mapper.api.EntityDtoMapper;
import com.github.avyvka.game.catalog.model.dto.DeveloperDto;
import com.github.avyvka.game.catalog.model.entity.DeveloperEntity;
import com.github.avyvka.game.catalog.repository.DeveloperRepository;
import com.github.avyvka.game.catalog.repository.api.CustomR2dbcRepository;
import com.github.avyvka.game.catalog.service.DeveloperService;
import com.github.avyvka.game.catalog.service.support.AbstractReactiveCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DefaultDeveloperService
        extends AbstractReactiveCrudService<DeveloperEntity, DeveloperDto, UUID>
        implements DeveloperService
{
    @Autowired
    protected DefaultDeveloperService(
            DeveloperRepository repository,
            EntityDtoMapper<DeveloperEntity, DeveloperDto> mapper
    ) {
        super(repository, mapper);
    }
}
