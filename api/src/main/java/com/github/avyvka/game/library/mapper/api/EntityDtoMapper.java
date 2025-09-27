package com.github.avyvka.game.library.mapper.api;

import org.mapstruct.*;

public interface EntityDtoMapper<E, D> {

    E toEntity(D dto);

    D toDto(E entity);

    E update(@MappingTarget E entity, D dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    E partialUpdate(@MappingTarget E entity, D dto);
}