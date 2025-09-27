package com.github.avyvka.game.library.repository;

import com.github.avyvka.game.library.model.entity.DeveloperEntity;
import com.github.avyvka.game.library.model.entity.PlatformEntity;
import com.github.avyvka.game.library.repository.api.CustomR2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeveloperRepository extends CustomR2dbcRepository<DeveloperEntity, UUID> {
}
