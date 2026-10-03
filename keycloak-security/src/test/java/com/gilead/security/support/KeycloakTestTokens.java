package com.gilead.security.support;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class KeycloakTestTokens {

    public static final String ISSUER = "http://localhost:8180/realms/gilead";
    public static final String AUDIENCE = "gilead-api";

    private KeycloakTestTokens() {
    }

    public static String withRealmRoles(String... roles) {
        return token(claims -> claims.claim("realm_access", Map.of("roles", List.of(roles))));
    }

    public static String withClientRoles(String clientId, String... roles) {
        return token(claims -> claims.claim(
                "resource_access",
                Map.of(clientId, Map.of("roles", List.of(roles)))));
    }

    public static String token(Consumer<JWTClaimsSet.Builder> customizer) {
        JWTClaimsSet.Builder claims = new JWTClaimsSet.Builder()
                .issuer(ISSUER)
                .subject("user-1")
                .audience(AUDIENCE)
                .claim("preferred_username", "ada")
                .claim("scope", "openid profile")
                .issueTime(Date.from(Instant.now()))
                .expirationTime(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)));
        customizer.accept(claims);
        return sign(claims.build());
    }

    public static String sign(JWTClaimsSet claims) {
        try {
            SignedJWT jwt = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256)
                            .keyID("test-key")
                            .type(JOSEObjectType.JWT)
                            .build(),
                    claims);
            jwt.sign(new RSASSASigner(TestRsaKeys.privateKey()));
            return jwt.serialize();
        } catch (JOSEException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
