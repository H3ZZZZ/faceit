package com.faceit.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FaceitConfig {

    @Value("${faceit.api.key}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}
