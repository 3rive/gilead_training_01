package com.gilead.security.config;

import com.gilead.security.security.KeycloakAudienceValidator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
@EnableConfigurationProperties(KeycloakSecurityProperties.class)
public class KeycloakJwtConfig {

    /**
     * Issuer, expiry, and audience. Signature checks stay on the decoder,
     * which loads Keycloak's JWKS in the default profile.
     */
    @Bean
    OAuth2TokenValidator<Jwt> keycloakTokenValidator(KeycloakSecurityProperties properties) {
        return new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefaultWithIssuer(properties.issuerUri().toString()),
                new KeycloakAudienceValidator(properties.audience()));
    }

    /**
     * Resolves Keycloak's signing keys from the realm. Tests replace this bean
     * so the suite does not need a running Keycloak.
     */
    @Bean
    @Profile("!test")
    NimbusJwtDecoder jwtDecoder(
            KeycloakSecurityProperties properties,
            OAuth2TokenValidator<Jwt> keycloakTokenValidator) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withIssuerLocation(properties.issuerUri().toString()).build();
        decoder.setJwtValidator(keycloakTokenValidator);
        return decoder;
    }
}
