package com.gilead.security.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Rejects access tokens that were minted for a different client.
 *
 * <p>Keycloak only puts the API client id into {@code aud} when an audience
 * mapper is configured. The bundled realm does that for {@code gilead-api}.
 */
public final class KeycloakAudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final String audience;

    public KeycloakAudienceValidator(String audience) {
        this.audience = audience;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        if (token.getAudience().contains(audience)) {
            return OAuth2TokenValidatorResult.success();
        }
        OAuth2Error error = new OAuth2Error(
                "invalid_token",
                "The token audience is not accepted",
                null);
        return OAuth2TokenValidatorResult.failure(error);
    }
}
