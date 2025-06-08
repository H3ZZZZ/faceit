package com.faceit.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FaceitConfig {

    @Value("${faceit.api.v1-base-url}")
    private String v1BaseUrl;

    @Value("${faceit.api.v4-base-url}")
    private String v4BaseUrl;

    @Value("${faceit.api.token}")
    private String faceitApiToken;

    @Bean("unauthenticatedWebClient")
    public WebClient unauthenticatedWebClient() {
        return WebClient.builder()
                .baseUrl(v1BaseUrl)
                .build();
    }

    @Bean("authenticatedWebClient")
    public WebClient authenticatedWebClient() {
        return WebClient.builder()
                .baseUrl(v4BaseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + faceitApiToken)
                .build();
    }
}

