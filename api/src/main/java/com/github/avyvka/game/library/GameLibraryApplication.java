package com.github.avyvka.game.library;

import com.github.avyvka.game.library.repository.support.CustomSimpleR2dbcRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories(repositoryBaseClass = CustomSimpleR2dbcRepository.class)
public class GameLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameLibraryApplication .class, args);
	}

}
