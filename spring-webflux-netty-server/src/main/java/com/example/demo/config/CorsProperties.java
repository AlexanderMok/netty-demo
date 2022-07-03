package com.example.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "security.cors", ignoreUnknownFields = true)
@Getter
@Setter
public class CorsProperties {

    @NonNull
    private List<String> allowedOriginPatterns;

    private List<String> allowedMethods = Collections.unmodifiableList(
            Arrays.asList(HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.POST.name(), HttpMethod.OPTIONS.name(), HttpMethod.PUT.name()));

    private List<String> allowedHeaders = Collections.singletonList(CorsConfiguration.ALL);

    private List<String> exposedHeaders = Collections.singletonList(CorsConfiguration.ALL);

    private Boolean allowCredentials = true;

    private Long maxAge = 1800L;


}
