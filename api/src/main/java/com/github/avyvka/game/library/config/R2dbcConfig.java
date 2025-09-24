package com.github.avyvka.game.library.config;

import com.github.avyvka.game.library.repository.support.CustomSimpleR2dbcRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(repositoryBaseClass = CustomSimpleR2dbcRepository.class)
public class R2dbcConfig {
}
