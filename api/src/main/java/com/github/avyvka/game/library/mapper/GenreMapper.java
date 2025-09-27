package com.github.avyvka.game.library.mapper;

import com.github.avyvka.game.library.mapper.api.EntityDtoMapper;
import com.github.avyvka.game.library.model.dto.GenreDto;
import com.github.avyvka.game.library.model.entity.GenreEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface GenreMapper extends EntityDtoMapper<GenreEntity, GenreDto> {
}