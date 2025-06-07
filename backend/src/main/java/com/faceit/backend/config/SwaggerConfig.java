package com.faceit.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI faceitStatsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Faceit Stats API")
                        .description("API til at hente spillerstatistik på CS2")
                        .version("v1.0"));
    }
}
