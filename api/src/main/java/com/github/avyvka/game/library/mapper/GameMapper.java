package com.github.avyvka.game.library.mapper;

import com.github.avyvka.game.library.mapper.api.EntityDtoMapper;
import com.github.avyvka.game.library.model.dto.GameDto;
import com.github.avyvka.game.library.model.entity.GameEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface GameMapper extends EntityDtoMapper<GameEntity, GameDto> {

    @Override
    @Mapping(target = "developerId", source = "developer.id")
    GameEntity toEntity(GameDto dto);

    @Override
    @Mapping(target = "developer.id", source = "developerId")
    GameDto toDto(GameEntity entity);

    @Override
    default GameEntity update(GameEntity entity, GameDto dto) {
        return (dto == null) ? null : new GameEntity(
                entity.id(),
                dto.name(),
                dto.developer().id()
        );
    }

    @Override
    default GameEntity partialUpdate(GameEntity entity, GameDto dto) {
        return (dto == null) ? null : new GameEntity(
                entity.id(),
                dto.name() != null ? dto.name() : entity.name(),
                dto.developer() != null &&  dto.developer().id() != null ? dto.developer().id() : entity.developerId()
        );
    }
}