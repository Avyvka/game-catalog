package com.github.avyvka.game.catalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import reactor.util.annotation.NonNull;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties(CorsMappingsProperties.class)
public class WebConfig implements WebFluxConfigurer {

    @Autowired
    CorsMappingsProperties corsMappingsProperties;

    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        var reactivePageableHandlerMethodArgumentResolver = new ReactivePageableHandlerMethodArgumentResolver();
        reactivePageableHandlerMethodArgumentResolver.setFallbackPageable(PageRequest.of(0, 20));
        configurer.addCustomResolver(reactivePageableHandlerMethodArgumentResolver);
    }

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        corsMappingsProperties.getMappings().forEach((_, p) -> {
            var mapping = registry.addMapping(p.getPattern());

            Optional.ofNullable(p.getAllowedOrigins())
                    .ifPresent(e -> mapping.allowedOrigins(e.toArray(String[]::new)));

            Optional.ofNullable(p.getAllowedMethods())
                    .ifPresent(e -> mapping.allowedMethods(e.toArray(String[]::new)));

            Optional.ofNullable(p.getAllowedHeaders())
                    .ifPresent(e -> mapping.allowedHeaders(e.toArray(String[]::new)));

            Optional.ofNullable(p.getAllowCredentials()).ifPresent(mapping::allowCredentials);
        });
    }
}