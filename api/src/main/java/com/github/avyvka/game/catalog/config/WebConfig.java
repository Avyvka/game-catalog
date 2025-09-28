package com.github.avyvka.game.catalog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

@Configuration
public class WebConfig implements WebFluxConfigurer {

    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        var reactivePageableHandlerMethodArgumentResolver = new ReactivePageableHandlerMethodArgumentResolver();
        reactivePageableHandlerMethodArgumentResolver.setFallbackPageable(PageRequest.of(0, 20));
        configurer.addCustomResolver(reactivePageableHandlerMethodArgumentResolver);
    }

}