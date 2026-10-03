package com.gilead.security.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeycloakAudienceValidatorTest {

    private final KeycloakAudienceValidator validator = new KeycloakAudienceValidator("gilead-api");

    @Test
    void acceptsTheConfiguredAudience() {
        Jwt jwt = jwt().audience(List.of("account", "gilead-api")).build();

        assertFalse(validator.validate(jwt).hasErrors());
    }

    @Test
    void rejectsATokenMintedForAnotherClient() {
        Jwt jwt = jwt().audience(List.of("account")).build();

        assertTrue(validator.validate(jwt).hasErrors());
    }

    private static Jwt.Builder jwt() {
        return Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("user-1")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60));
    }
}
