package com.github.avyvka.game.library.mapper;

import com.github.avyvka.game.library.model.dto.PlatformDto;
import com.github.avyvka.game.library.model.entity.PlatformEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PlatformMapper extends EntityDtoMapper<PlatformEntity, PlatformDto> {
}