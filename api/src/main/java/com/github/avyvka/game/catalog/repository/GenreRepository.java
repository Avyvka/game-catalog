package com.github.avyvka.game.catalog.repository;

import com.github.avyvka.game.catalog.model.entity.GenreEntity;
import com.github.avyvka.game.catalog.repository.api.CustomR2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenreRepository extends CustomR2dbcRepository<GenreEntity, UUID> {
}
