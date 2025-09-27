package com.github.avyvka.game.library.service;

import com.github.avyvka.game.library.mapper.api.EntityDtoMapper;
import com.github.avyvka.game.library.model.dto.DeveloperDto;
import com.github.avyvka.game.library.model.entity.DeveloperEntity;
import com.github.avyvka.game.library.repository.api.CustomR2dbcRepository;
import com.github.avyvka.game.library.service.support.AbstractReactiveCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeveloperService extends AbstractReactiveCrudService<DeveloperEntity, DeveloperDto, UUID> {

    @Autowired
    protected DeveloperService(
            CustomR2dbcRepository<DeveloperEntity, UUID> repository,
            EntityDtoMapper<DeveloperEntity, DeveloperDto> mapper
    ) {
        super(repository, mapper);
    }
}
