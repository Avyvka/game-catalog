package com.github.avyvka.game.library.mapper;

import com.github.avyvka.game.library.mapper.api.EntityDtoMapper;
import com.github.avyvka.game.library.model.dto.DeveloperDto;
import com.github.avyvka.game.library.model.entity.DeveloperEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DeveloperMapper extends EntityDtoMapper<DeveloperEntity, DeveloperDto> {
}