package com.github.avyvka.game.library.service.impl;

import com.github.avyvka.game.library.mapper.api.EntityDtoMapper;
import com.github.avyvka.game.library.model.dto.PlatformDto;
import com.github.avyvka.game.library.model.entity.PlatformEntity;
import com.github.avyvka.game.library.repository.PlatformRepository;
import com.github.avyvka.game.library.service.PlatformService;
import com.github.avyvka.game.library.service.support.AbstractReactiveCrudService;
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
