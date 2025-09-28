package com.github.avyvka.game.catalog.mapper;

import com.github.avyvka.game.catalog.mapper.api.EntityDtoMapper;
import com.github.avyvka.game.catalog.model.dto.DeveloperDto;
import com.github.avyvka.game.catalog.model.dto.PlatformDto;
import com.github.avyvka.game.catalog.model.entity.DeveloperEntity;
import com.github.avyvka.game.catalog.model.entity.PlatformEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PlatformMapper extends EntityDtoMapper<PlatformEntity, PlatformDto> {

    @Override
    default PlatformEntity update(PlatformEntity entity, PlatformDto dto) {
        return (dto == null) ? null : new PlatformEntity(
                entity.id(),
                dto.name(),
                dto.manufacturer()
        );
    }

    @Override
    default PlatformEntity partialUpdate(PlatformEntity entity, PlatformDto dto) {
        return (dto == null) ? null : new PlatformEntity(
                entity.id(),
                dto.name() != null ? dto.name() : entity.name(),
                dto.manufacturer() != null ? dto.manufacturer() : entity.manufacturer()
        );
    }
}