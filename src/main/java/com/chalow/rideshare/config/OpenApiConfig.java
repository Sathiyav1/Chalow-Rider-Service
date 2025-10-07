package com.chalow.rideshare.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI riderOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("RiderService API").version("v1").description("APIs for rider operations"));
    }
}
