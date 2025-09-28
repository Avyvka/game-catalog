package com.github.avyvka.game.catalog.mapper;

import com.github.avyvka.game.catalog.mapper.api.EntityDtoMapper;
import com.github.avyvka.game.catalog.model.dto.DeveloperDto;
import com.github.avyvka.game.catalog.model.entity.DeveloperEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DeveloperMapper extends EntityDtoMapper<DeveloperEntity, DeveloperDto> {

    @Override
    default DeveloperEntity update(DeveloperEntity entity, DeveloperDto dto) {
        return (dto == null) ? null : new DeveloperEntity(
                entity.id(),
                dto.name(),
                dto.description()
        );
    }

    @Override
    default DeveloperEntity partialUpdate(DeveloperEntity entity, DeveloperDto dto) {
        return (dto == null) ? null : new DeveloperEntity(
                entity.id(),
                dto.name() != null ? dto.name() : entity.name(),
                dto.description() != null ? dto.description() : entity.description()
        );
    }
}