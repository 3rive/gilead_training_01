package com.gilead.security.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Missing or rejected bearer tokens become a JSON problem response.
 *
 * <p>The {@code WWW-Authenticate} header still follows the bearer scheme so
 * clients can tell a token failure from a permission failure.
 */
@Component
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final SecurityProblemWriter writer;

    public ApiAuthenticationEntryPoint(SecurityProblemWriter writer) {
        this.writer = writer;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        response.setHeader("WWW-Authenticate", "Bearer");
        writer.write(response, HttpStatus.UNAUTHORIZED, "A valid bearer token is required.");
    }
}
