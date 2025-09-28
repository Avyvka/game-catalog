package com.github.avyvka.game.catalog.config;

import com.github.avyvka.game.catalog.GameCatalogApplication;
import com.github.avyvka.game.catalog.repository.support.CustomSimpleR2dbcRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(
        basePackageClasses = GameCatalogApplication.class,
        repositoryBaseClass = CustomSimpleR2dbcRepository.class
)
public class R2dbcConfig {
}
