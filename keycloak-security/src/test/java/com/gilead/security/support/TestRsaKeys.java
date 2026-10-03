package com.gilead.security.support;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * One RSA key pair for the whole test run. The test decoder trusts the public
 * half; tokens are signed with the private half.
 */
public final class TestRsaKeys {

    private static final RSAKey KEY = generate();

    private TestRsaKeys() {
    }

    public static RSAPublicKey publicKey() {
        try {
            return KEY.toRSAPublicKey();
        } catch (JOSEException exception) {
            throw new IllegalStateException(exception);
        }
    }

    public static RSAPrivateKey privateKey() {
        try {
            return KEY.toRSAPrivateKey();
        } catch (JOSEException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private static RSAKey generate() {
        try {
            return new RSAKeyGenerator(2048).keyID("test-key").generate();
        } catch (JOSEException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
