package com.gilead.security.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeycloakGrantedAuthoritiesConverterTest {

    private final KeycloakGrantedAuthoritiesConverter converter = new KeycloakGrantedAuthoritiesConverter("gilead-api");

    @Test
    void mapsRealmRolesClientRolesAndScopes() {
        Jwt jwt = jwt()
                .claim("scope", "library.read")
                .claim("realm_access", Map.of("roles", List.of("reader", "ROLE_admin")))
                .claim("resource_access", Map.of(
                        "gilead-api", Map.of("roles", List.of("editor")),
                        "account", Map.of("roles", List.of("manage-account"))))
                .build();

        List<String> authorities = converter.convert(jwt).stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertEquals(List.of("SCOPE_library.read", "ROLE_reader", "ROLE_admin", "ROLE_editor"), authorities);
    }

    @Test
    void ignoresBlankRolesAndMissingClaims() {
        Jwt jwt = jwt()
                .claim("realm_access", Map.of("roles", List.of("  ", 12)))
                .build();

        assertTrue(converter.convert(jwt).isEmpty());
    }

    private static Jwt.Builder jwt() {
        return Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("user-1")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60));
    }
}
