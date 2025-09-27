package com.github.avyvka.game.library.service;

import com.github.avyvka.game.library.mapper.api.EntityDtoMapper;
import com.github.avyvka.game.library.model.dto.GameDto;
import com.github.avyvka.game.library.model.dto.PlatformDto;
import com.github.avyvka.game.library.model.entity.GameEntity;
import com.github.avyvka.game.library.model.entity.PlatformEntity;
import com.github.avyvka.game.library.repository.api.CustomR2dbcRepository;
import com.github.avyvka.game.library.service.support.AbstractReactiveCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameService extends AbstractReactiveCrudService<GameEntity, GameDto, UUID> {

    @Autowired
    protected GameService(
            CustomR2dbcRepository<GameEntity, UUID> repository,
            EntityDtoMapper<GameEntity, GameDto> mapper
    ) {
        super(repository, mapper);
    }
}
