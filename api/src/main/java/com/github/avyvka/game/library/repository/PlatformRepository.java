package com.github.avyvka.game.library.repository;

import com.github.avyvka.game.library.model.entity.PlatformEntity;
import com.github.avyvka.game.library.repository.api.CustomR2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlatformRepository extends CustomR2dbcRepository<PlatformEntity, UUID> {
}
