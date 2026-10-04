# LinkedIn post

Copy everything below the line.

---

Your Spring Boot API should never see the user's password.

That job belongs to Keycloak. A person signs in there. Keycloak returns a signed access token. The API checks the token and decides what that person may do.

Many tutorials still connect Spring Boot with the Keycloak adapter. Keycloak removed that adapter. The current path is Spring Security's OAuth2 resource server, the same OpenID Connect setup you would use with any other issuer.

I built a small library API so the pieces are easy to see.

Ada has the reader role. She can open the catalog. She cannot publish a notice.
An admin can publish.
Health is open, and needs no token.

On every call the API checks the signature against Keycloak's keys, then the issuer, the expiry, and the audience. Realm roles and this API's client roles become ordinary Spring Security roles.

Keycloak 26 starts from Docker Compose with the realm, the client, and both users already imported. The tests sign tokens locally, so you can read the security rules without running Keycloak first.

Code: https://github.com/3rive/gilead_training_01/tree/aigen/keycloak-spring-security-68d4/keycloak-security

#Keycloak #SpringBoot #Java #OAuth2 #OpenIDConnect
