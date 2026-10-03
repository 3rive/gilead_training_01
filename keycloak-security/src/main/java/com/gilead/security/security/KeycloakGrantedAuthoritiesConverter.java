package com.gilead.security.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Turns a Keycloak access token into Spring Security authorities.
 *
 * <p>Scopes stay {@code SCOPE_*} via the standard converter. Realm roles and
 * this API's client roles become {@code ROLE_*} so {@code hasRole} matches
 * them. Roles of other clients, such as {@code account}, are ignored.
 */
public final class KeycloakGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    static final String REALM_ACCESS = "realm_access";
    static final String RESOURCE_ACCESS = "resource_access";
    static final String ROLES = "roles";

    private final String clientId;
    private final Converter<Jwt, Collection<GrantedAuthority>> scopes = new JwtGrantedAuthoritiesConverter();

    public KeycloakGrantedAuthoritiesConverter(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException("clientId is required");
        }
        this.clientId = clientId;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        Collection<GrantedAuthority> scopeAuthorities = scopes.convert(jwt);
        if (scopeAuthorities != null) {
            authorities.addAll(scopeAuthorities);
        }
        roleNames(jwt.getClaim(REALM_ACCESS)).forEach(role -> authorities.add(authority(role)));
        roleNames(clientAccess(jwt)).forEach(role -> authorities.add(authority(role)));
        return List.copyOf(authorities);
    }

    private Object clientAccess(Jwt jwt) {
        Object resourceAccess = jwt.getClaim(RESOURCE_ACCESS);
        if (!(resourceAccess instanceof Map<?, ?> clients)) {
            return null;
        }
        return clients.get(clientId);
    }

    private static Stream<String> roleNames(Object accessClaim) {
        if (!(accessClaim instanceof Map<?, ?> access)) {
            return Stream.empty();
        }
        Object roles = access.get(ROLES);
        if (!(roles instanceof Collection<?> values)) {
            return Stream.empty();
        }
        return values.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(String::trim)
                .filter(role -> !role.isEmpty());
    }

    private static SimpleGrantedAuthority authority(String role) {
        String granted = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return new SimpleGrantedAuthority(granted);
    }
}
