package com.github.avyvka.game.catalog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties("spring.webflux.cors")
class CorsMappingsProperties {

    private Map<String, CorsMappingProperties> mappings;

    public Map<String, CorsMappingProperties> getMappings() {
        return mappings;
    }

    public void setMappings(Map<String, CorsMappingProperties> mappings) {
        this.mappings = mappings;
    }

    static class CorsMappingProperties {

        private String pattern;

        private List<String> allowedOrigins;

        private List<String> allowedMethods;

        private List<String> allowedHeaders;

        private Boolean allowCredentials;

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public List<String> getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(List<String> allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public List<String> getAllowedHeaders() {
            return allowedHeaders;
        }

        public void setAllowedHeaders(List<String> allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        public Boolean getAllowCredentials() {
            return allowCredentials;
        }

        public void setAllowCredentials(Boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }

    }

}