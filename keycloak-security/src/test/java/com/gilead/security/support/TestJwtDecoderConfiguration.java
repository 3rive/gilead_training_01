package com.gilead.security.support;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
@Profile("test")
public class TestJwtDecoderConfiguration {

    @Bean
    NimbusJwtDecoder jwtDecoder(OAuth2TokenValidator<Jwt> keycloakTokenValidator) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(TestRsaKeys.publicKey()).build();
        decoder.setJwtValidator(keycloakTokenValidator);
        return decoder;
    }
}
