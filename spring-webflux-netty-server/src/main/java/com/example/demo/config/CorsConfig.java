package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CorsConfig implements WebFluxConfigurer {

    private final CorsProperties corsProperties;

    public CorsConfig(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(this.corsProperties.getAllowCredentials())
                .allowedOriginPatterns(this.corsProperties.getAllowedOriginPatterns().toArray(new String[0]))
                .allowedMethods(this.corsProperties.getAllowedMethods().toArray(new String[0]))
                .allowedHeaders(this.corsProperties.getAllowedHeaders().toArray(new String[0]))
                .exposedHeaders(this.corsProperties.getExposedHeaders().toArray(new String[0]))
                .maxAge(this.corsProperties.getMaxAge());
    }
}
