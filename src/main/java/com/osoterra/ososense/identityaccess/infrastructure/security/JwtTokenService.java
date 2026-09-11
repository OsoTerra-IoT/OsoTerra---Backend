package com.osoterra.ososense.identityaccess.infrastructure.security;

import com.osoterra.ososense.identityaccess.domain.model.UserAccount;
import com.osoterra.ososense.identityaccess.domain.gateways.IssuedToken;
import com.osoterra.ososense.identityaccess.domain.gateways.TokenIssuer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

/**
 * Issues and validates the JWT access tokens used by the RESTful API.
 */
@Component
public class JwtTokenService implements TokenIssuer {

    private final SecretKey signingKey;
    private final Duration validity;

    public JwtTokenService(
            @Value("${app.security.jwt.secret}") String base64Secret,
            @Value("${app.security.jwt.expiration-minutes:60}") long expirationMinutes) {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
        this.validity = Duration.ofMinutes(expirationMinutes);
    }

    @Override
    public IssuedToken issueFor(UserAccount account) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(validity);

        String token = Jwts.builder()
                .subject(account.getId().value().toString())
                .claim("role", account.getRole().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();

        return new IssuedToken(token, expiresAt);
    }

    /**
     * Returns the authenticated principal when the token is well-formed, signed by this
     * service, and not expired.
     */
    public Optional<AuthenticatedPrincipal> validate(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Optional.of(new AuthenticatedPrincipal(
                    Long.valueOf(claims.getSubject()), claims.get("role", String.class)));
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public record AuthenticatedPrincipal(Long userId, String role) {
    }
}
