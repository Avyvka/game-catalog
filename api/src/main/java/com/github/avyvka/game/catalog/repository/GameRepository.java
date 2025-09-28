package com.github.avyvka.game.catalog.repository;

import com.github.avyvka.game.catalog.model.entity.GameEntity;
import com.github.avyvka.game.catalog.repository.api.CustomR2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameRepository extends CustomR2dbcRepository<GameEntity, UUID> {
}
