package com.gilead.security.api;

import com.gilead.security.support.KeycloakTestTokens;
import com.gilead.security.support.TestJwtDecoderConfiguration;
import com.nimbusds.jwt.JWTClaimsSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static com.gilead.security.support.KeycloakTestTokens.AUDIENCE;
import static com.gilead.security.support.KeycloakTestTokens.ISSUER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestJwtDecoderConfiguration.class)
class LibraryApiSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicHealthDoesNotRequireAToken() throws Exception {
        mockMvc.perform(get("/api/public/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("up"));
    }

    @Test
    void missingTokenIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("WWW-Authenticate", "Bearer"))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.title").value("Unauthorized"));
    }

    @Test
    void tamperedTokenIsUnauthorized() throws Exception {
        String token = KeycloakTestTokens.withRealmRoles("reader") + "x";

        mockMvc.perform(get("/api/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void expiredTokenIsUnauthorized() throws Exception {
        Instant past = Instant.now().minus(10, ChronoUnit.MINUTES);
        String token = KeycloakTestTokens.sign(new JWTClaimsSet.Builder()
                .issuer(ISSUER)
                .subject("user-1")
                .audience(AUDIENCE)
                .issueTime(Date.from(past))
                .expirationTime(Date.from(past.plus(1, ChronoUnit.MINUTES)))
                .build());

        mockMvc.perform(get("/api/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void wrongIssuerIsUnauthorized() throws Exception {
        String token = KeycloakTestTokens.sign(new JWTClaimsSet.Builder()
                .issuer("http://localhost:8180/realms/other")
                .subject("user-1")
                .audience(AUDIENCE)
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)))
                .claim("realm_access", java.util.Map.of("roles", List.of("reader")))
                .build());

        mockMvc.perform(get("/api/library/books").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticatedCallerWithoutReaderRoleCannotListBooks() throws Exception {
        String token = KeycloakTestTokens.withRealmRoles("uma_authorization");

        mockMvc.perform(get("/api/library/books").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void wrongAudienceIsUnauthorized() throws Exception {
        String token = KeycloakTestTokens.sign(new JWTClaimsSet.Builder()
                .issuer(ISSUER)
                .subject("user-1")
                .audience(List.of("account"))
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)))
                .claim("realm_access", java.util.Map.of("roles", List.of("reader")))
                .build());

        mockMvc.perform(get("/api/library/books").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void readerCanSeeTheCatalogAndTheirAccount() throws Exception {
        String token = KeycloakTestTokens.withRealmRoles("reader");

        mockMvc.perform(get("/api/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject").value("user-1"))
                .andExpect(jsonPath("$.username").value("ada"))
                .andExpect(jsonPath("$.roles[0]").value("reader"))
                .andExpect(jsonPath("$.scopes[0]").value("openid"))
                .andExpect(jsonPath("$.scopes[1]").value("profile"));

        mockMvc.perform(get("/api/library/books").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("9780134685991"));
    }

    @Test
    void clientRoleGrantsTheSameAccessAsARealmRole() throws Exception {
        String token = KeycloakTestTokens.withClientRoles("gilead-api", "reader");

        mockMvc.perform(get("/api/library/books").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void readerCannotPublishNotices() throws Exception {
        String token = KeycloakTestTokens.withRealmRoles("reader");

        mockMvc.perform(post("/api/admin/notices")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"hours changed\"}"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void adminCanPublishAndListNotices() throws Exception {
        String token = KeycloakTestTokens.withRealmRoles("admin", "reader");

        mockMvc.perform(post("/api/admin/notices")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Reading room opens at nine\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("Reading room opens at nine"))
                .andExpect(jsonPath("$.author").value("ada"));

        mockMvc.perform(get("/api/admin/notices").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author").value("ada"));
    }

    @Test
    void blankNoticeIsRejected() throws Exception {
        String token = KeycloakTestTokens.withRealmRoles("admin");

        mockMvc.perform(post("/api/admin/notices")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"  \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("text")));
    }
}
