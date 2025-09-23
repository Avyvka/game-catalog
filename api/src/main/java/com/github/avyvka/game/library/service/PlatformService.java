package com.github.avyvka.game.library.service;

import com.github.avyvka.game.library.mapper.EntityDtoMapper;
import com.github.avyvka.game.library.model.dto.PlatformDto;
import com.github.avyvka.game.library.model.entity.PlatformEntity;
import com.github.avyvka.game.library.repository.api.CustomR2dbcRepository;
import com.github.avyvka.game.library.service.support.AbstractReactiveCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlatformService extends AbstractReactiveCrudService<PlatformEntity, PlatformDto, UUID> {

    @Autowired
    protected PlatformService(
            CustomR2dbcRepository<PlatformEntity, UUID> repository,
            EntityDtoMapper<PlatformEntity, PlatformDto> mapper
    ) {
        super(repository, mapper);
    }
}
