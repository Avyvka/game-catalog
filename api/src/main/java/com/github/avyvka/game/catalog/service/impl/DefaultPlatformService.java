package com.github.avyvka.game.catalog.service.impl;

import com.github.avyvka.game.catalog.mapper.api.EntityDtoMapper;
import com.github.avyvka.game.catalog.model.dto.PlatformDto;
import com.github.avyvka.game.catalog.model.entity.PlatformEntity;
import com.github.avyvka.game.catalog.repository.PlatformRepository;
import com.github.avyvka.game.catalog.service.PlatformService;
import com.github.avyvka.game.catalog.service.support.AbstractReactiveCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DefaultPlatformService
        extends AbstractReactiveCrudService<PlatformEntity, PlatformDto, UUID>
        implements PlatformService
{
    @Autowired
    protected DefaultPlatformService(
            PlatformRepository repository,
            EntityDtoMapper<PlatformEntity, PlatformDto> mapper
    ) {
        super(repository, mapper);
    }
}
