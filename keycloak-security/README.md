# Keycloak security for a Spring Boot API

A small library API secured the way current Keycloak expects: **Spring Security's OAuth 2.0 resource server**, not the removed Keycloak Spring adapter.

Keycloak 26 signs access tokens. This API checks the signature against the realm's keys, then checks the issuer, the expiry, and the audience. Realm roles and this API's client roles become Spring Security authorities.

| Call | Who can make it |
| --- | --- |
| `GET /api/public/health` | anyone |
| `GET /api/me` | any valid access token |
| `GET /api/library/books` | realm or client role `reader` |
| `GET /api/admin/notices` and `POST /api/admin/notices` | role `admin` |

## Run it locally

Start Keycloak 26.8 with the `gilead` realm, two users, and the `gilead-api` client already imported:

```bash
cd keycloak-security
docker compose up
```

Keycloak listens on <http://localhost:8180>. The admin console login is `admin` / `admin`.

In another terminal, start the API (it reads the realm's signing keys on startup, so Keycloak has to be up first):

```bash
mvn spring-boot:run
```

Ask Keycloak for a token, then call the API. The password grant is enabled on this local client so the demo is a single request. Do not leave that grant on for a production client.

Reader (`ada` / `ada`):

```bash
TOKEN=$(curl -s -X POST 'http://localhost:8180/realms/gilead/protocol/openid-connect/token' \
  -d 'client_id=gilead-api' \
  -d 'client_secret=gilead-api-secret' \
  -d 'grant_type=password' \
  -d 'username=ada' \
  -d 'password=ada' | python3 -c 'import json,sys; print(json.load(sys.stdin)["access_token"])')

curl -s localhost:8080/api/me -H "Authorization: Bearer $TOKEN"
curl -s localhost:8080/api/library/books -H "Authorization: Bearer $TOKEN"
curl -s -o /dev/null -w '%{http_code}\n' -X POST localhost:8080/api/admin/notices \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"text":"hours changed"}'
```

Admin (`admin` / `admin`) can publish a notice:

```bash
ADMIN=$(curl -s -X POST 'http://localhost:8180/realms/gilead/protocol/openid-connect/token' \
  -d 'client_id=gilead-api' \
  -d 'client_secret=gilead-api-secret' \
  -d 'grant_type=password' \
  -d 'username=admin' \
  -d 'password=admin' | python3 -c 'import json,sys; print(json.load(sys.stdin)["access_token"])')

curl -s -X POST localhost:8080/api/admin/notices \
  -H "Authorization: Bearer $ADMIN" \
  -H 'Content-Type: application/json' \
  -d '{"text":"Reading room opens at nine"}'
```

`GET /api/public/health` needs no token.

## Point it at your own realm

```bash
export KEYCLOAK_ISSUER_URI=https://id.example.com/realms/your-realm
export KEYCLOAK_CLIENT_ID=your-api
export KEYCLOAK_AUDIENCE=your-api
mvn spring-boot:run
```

The access token must include:

- `iss` equal to `KEYCLOAK_ISSUER_URI`
- `aud` containing `KEYCLOAK_AUDIENCE` (Keycloak audience mapper, included in `keycloak/gilead-realm.json`)
- roles in `realm_access.roles` or `resource_access.<client-id>.roles`

Roles are mapped to `ROLE_<name>`, so `hasRole("reader")` matches a Keycloak role named `reader`. Scopes stay `SCOPE_*`. The caller name is `preferred_username`, or `sub` when that claim is absent.

Browser apps on `http://localhost:4200` and `http://localhost:5173` are allowed to call `/api/**` with an `Authorization` header. Change `app.security.allowed-origins` in `application.yml` for other origins.

## Tests

The tests sign tokens with a local RSA key and run them through the same issuer, expiry, and audience checks the API uses against Keycloak. No Keycloak process is required.

```bash
mvn test
```

## What this project deliberately does not use

`keycloak-spring-boot-starter` and `keycloak-spring-security-adapter` were removed. Keycloak's own securing-apps guide tells Spring Boot services to use Spring Security's OpenID Connect support instead. This project follows that: `spring-boot-starter-security-oauth2-resource-server` on Spring Boot 4.1, with a Keycloak-shaped role converter.
