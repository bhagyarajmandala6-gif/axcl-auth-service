package com.innocito.axcl.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Map;

import static com.innocito.axcl.util.ApplicationConstants.ACTIVE_PROFILE;
import static com.innocito.axcl.util.ApplicationConstants.RELEASE_TIMESTAMP;
import static com.innocito.axcl.util.PropertyNameConstants.APP_VERSION;
import static com.innocito.axcl.util.PropertyNameConstants.SPRING_PROFILES_ACTIVE;

@Configuration
@OpenAPIDefinition(info = @Info(title = "AXCL External Integration Service API", version = "1.0",
        description = "AXCL External Integration Service API Description"),
        security = {@SecurityRequirement(name = "Bearer Token")}
)
@SecuritySchemes({
        @SecurityScheme(name = "Bearer Token", type = SecuritySchemeType.HTTP,
                scheme = "bearer", bearerFormat = "JWT")
})
public class OpenApiConfig {
    @Value(APP_VERSION)
    private String projectVersion;
    @Value(SPRING_PROFILES_ACTIVE)
    private String activeProfile;

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            openApi.getInfo().setVersion(projectVersion);
            openApi.getInfo().setExtensions(Map.of(RELEASE_TIMESTAMP, new Date(), ACTIVE_PROFILE, activeProfile));
        };
    }
}