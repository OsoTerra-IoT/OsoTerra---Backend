package com.osoterra.ososense.iam.infrastructure.security;

import com.osoterra.ososense.iam.domain.model.UserAccount;
import com.osoterra.ososense.iam.domain.gateways.IssuedToken;
import com.osoterra.ososense.iam.domain.gateways.TokenIssuer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        this.signingKey = Keys.hmacShaKeyFor(keyBytes(base64Secret));
        this.validity = Duration.ofMinutes(expirationMinutes);
    }

    /**
     * Uses the secret as Base64 when it decodes to the 256 bits HS256 needs; otherwise the
     * secret is treated as a passphrase and stretched to 256 bits with SHA-256, so a plain
     * text {@code JWT_SECRET} set on the hosting platform still yields a valid key.
     */
    static byte[] keyBytes(String secret) {
        try {
            byte[] decoded = Decoders.BASE64.decode(secret);
            if (decoded.length >= 32) {
                return decoded;
            }
        } catch (RuntimeException notBase64) {
            // Fall through to the passphrase derivation.
        }
        try {
            return MessageDigest.getInstance("SHA-256").digest(secret.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
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
