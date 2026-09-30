package com.osoterra.ososense.shared.infrastructure.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Publishes the API description at {@code /v3/api-docs/{group}} and the interactive explorer
 * at {@code /swagger-ui.html}, with the bearer JWT scheme wired in so authenticated
 * endpoints can be tried directly from the UI. Each bounded context gets its own group,
 * selectable from the explorer's definition dropdown, plus an {@code all} group.
 */
@Configuration
class OpenApiConfiguration {

    private static final String BEARER_SCHEME = "bearerAuth";
    private static final String BASE_PACKAGE = "com.osoterra.ososense";

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

    @Bean
    GroupedOpenApi allApi() {
        return group("all", "All modules", BASE_PACKAGE);
    }

    @Bean
    GroupedOpenApi iamApi() {
        return group("iam", "Identity and Access Management", BASE_PACKAGE + ".iam");
    }

    @Bean
    GroupedOpenApi subscriptionBillingApi() {
        return group("subscription-billing", "Subscription and Billing", BASE_PACKAGE + ".subscriptionbilling");
    }

    @Bean
    GroupedOpenApi farmManagementApi() {
        return group("farm-management", "Farm Management", BASE_PACKAGE + ".farmmanagement");
    }

    @Bean
    GroupedOpenApi soilMonitoringApi() {
        return group("soil-monitoring", "Soil Monitoring", BASE_PACKAGE + ".soilmonitoring");
    }

    @Bean
    GroupedOpenApi salinityAlertingApi() {
        return group("salinity-alerting", "Salinity Alerting", BASE_PACKAGE + ".salinityalerting");
    }

    @Bean
    GroupedOpenApi analyticsReportingApi() {
        return group("analytics-reporting", "Analytics and Reporting", BASE_PACKAGE + ".analyticsreporting");
    }

    private static GroupedOpenApi group(String name, String displayName, String packageToScan) {
        return GroupedOpenApi.builder()
                .group(name)
                .displayName(displayName)
                .packagesToScan(packageToScan)
                .build();
    }
}
