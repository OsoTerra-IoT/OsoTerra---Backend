package com.osoterra.ososense.shared.infrastructure.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Publishes the API description at {@code /v3/api-docs} and the interactive explorer at
 * {@code /swagger-ui.html}, with the bearer JWT scheme wired in so authenticated
 * endpoints can be tried directly from the UI.
 */
@Configuration
class OpenApiConfiguration {

    private static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    OpenAPI osoterraOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("OsoTerra IoT API")
                        .description("REST API for the OsoTerra soil salinity monitoring platform")
                        .version("v1"))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME));
    }
}
