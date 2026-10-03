package com.gilead.security.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Builds the authentication for a Keycloak access token.
 *
 * <p>The principal name is {@code preferred_username} when Keycloak sent it,
 * and the {@code sub} claim otherwise.
 */
public final class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final KeycloakGrantedAuthoritiesConverter authoritiesConverter;

    public KeycloakJwtAuthenticationConverter(String clientId) {
        this.authoritiesConverter = new KeycloakGrantedAuthoritiesConverter(clientId);
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        String principal = username == null || username.isBlank() ? jwt.getSubject() : username;
        return new JwtAuthenticationToken(jwt, authoritiesConverter.convert(jwt), principal);
    }
}
