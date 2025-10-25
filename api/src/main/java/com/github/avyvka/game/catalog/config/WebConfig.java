package com.github.avyvka.game.catalog.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

@Configuration
@EnableConfigurationProperties(CorsMappingsProperties.class)
public class WebConfig implements WebFluxConfigurer {

    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        var reactivePageableHandlerMethodArgumentResolver = new ReactivePageableHandlerMethodArgumentResolver();
        reactivePageableHandlerMethodArgumentResolver.setFallbackPageable(PageRequest.of(0, 20));
        configurer.addCustomResolver(reactivePageableHandlerMethodArgumentResolver);
    }

    @Bean
    @ConditionalOnProperty("spring.webflux.cors.enabled")
    UrlBasedCorsConfigurationSource corsConfigurationSource(CorsMappingsProperties corsMappingsProperties) {
        var registry = new UrlBasedCorsConfigurationSource();

        if (corsMappingsProperties.getMappings() != null) {
            corsMappingsProperties.getMappings().forEach((_, mapping) -> {
                var cors = new CorsConfiguration();
                if (mapping.getAllowedOrigins() != null) {
                    cors.setAllowedOrigins(mapping.getAllowedOrigins());
                }
                if (mapping.getAllowedMethods() != null) {
                    cors.setAllowedMethods(mapping.getAllowedMethods());
                }
                if (mapping.getAllowedHeaders() != null) {
                    cors.setAllowedHeaders(mapping.getAllowedHeaders());
                }
                if (mapping.getAllowCredentials() != null) {
                    cors.setAllowCredentials(mapping.getAllowCredentials());
                }
                registry.registerCorsConfiguration(mapping.getPattern(), cors);
            });
        }

        return registry;
    }
}