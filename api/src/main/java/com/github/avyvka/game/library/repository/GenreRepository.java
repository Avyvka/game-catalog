package com.github.avyvka.game.library.repository;

import com.github.avyvka.game.library.model.entity.GenreEntity;
import com.github.avyvka.game.library.repository.api.CustomR2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenreRepository extends CustomR2dbcRepository<GenreEntity, UUID> {
}
