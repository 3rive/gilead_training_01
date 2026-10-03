package com.gilead.security.config;

import com.gilead.security.security.ApiAccessDeniedHandler;
import com.gilead.security.security.ApiAuthenticationEntryPoint;
import com.gilead.security.security.KeycloakJwtAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter(KeycloakSecurityProperties properties) {
        return new KeycloakJwtAuthenticationConverter(properties.clientId());
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            KeycloakSecurityProperties properties,
            KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter,
            ApiAuthenticationEntryPoint authenticationEntryPoint,
            ApiAccessDeniedHandler accessDeniedHandler) throws Exception {
        http
                // Bearer tokens are not sent automatically by the browser, so cookie CSRF does not apply.
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/public/**", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/library/**").hasRole("reader")
                        .requestMatchers("/api/admin/**").hasRole("admin")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
                        .jwtAuthenticationConverter(keycloakJwtAuthenticationConverter)));

        if (!properties.allowedOrigins().isEmpty()) {
            http.cors(cors -> cors.configurationSource(corsConfigurationSource(properties)));
        }
        return http.build();
    }

    private static CorsConfigurationSource corsConfigurationSource(KeycloakSecurityProperties properties) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(properties.allowedOrigins());
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
