package com.gilead.security.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class KeycloakJwtAuthenticationConverterTest {

    private final KeycloakJwtAuthenticationConverter converter =
            new KeycloakJwtAuthenticationConverter("gilead-api");

    @Test
    void prefersTheKeycloakUsername() {
        Jwt jwt = jwt().claim("preferred_username", "ada").build();

        AbstractAuthenticationToken authentication = converter.convert(jwt);

        assertEquals("ada", authentication.getName());
        assertInstanceOf(Jwt.class, authentication.getPrincipal());
    }

    @Test
    void fallsBackToTheSubject() {
        Jwt jwt = jwt().build();

        assertEquals("user-1", converter.convert(jwt).getName());
    }

    private static Jwt.Builder jwt() {
        return Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("user-1")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60));
    }
}
