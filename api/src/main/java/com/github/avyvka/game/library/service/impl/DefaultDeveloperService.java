package com.github.avyvka.game.library.service.impl;

import com.github.avyvka.game.library.mapper.api.EntityDtoMapper;
import com.github.avyvka.game.library.model.dto.DeveloperDto;
import com.github.avyvka.game.library.model.entity.DeveloperEntity;
import com.github.avyvka.game.library.repository.DeveloperRepository;
import com.github.avyvka.game.library.repository.api.CustomR2dbcRepository;
import com.github.avyvka.game.library.service.DeveloperService;
import com.github.avyvka.game.library.service.support.AbstractReactiveCrudService;
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
