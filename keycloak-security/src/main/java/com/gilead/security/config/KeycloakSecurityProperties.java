package com.gilead.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.util.List;

/**
 * Where this API expects Keycloak tokens to come from.
 *
 * <p>{@code issuerUri} is the realm issuer ({@code iss}). {@code clientId}
 * selects which {@code resource_access} entry is treated as this API's roles.
 * {@code audience} must appear in the token {@code aud} claim.
 */
@ConfigurationProperties(prefix = "app.security")
public record KeycloakSecurityProperties(
        URI issuerUri,
        String clientId,
        String audience,
        List<String> allowedOrigins) {

    public KeycloakSecurityProperties {
        if (issuerUri == null) {
            throw new IllegalArgumentException("app.security.issuer-uri is required");
        }
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException("app.security.client-id is required");
        }
        if (audience == null || audience.isBlank()) {
            throw new IllegalArgumentException("app.security.audience is required");
        }
        allowedOrigins = allowedOrigins == null ? List.of() : List.copyOf(allowedOrigins);
    }
}
