package sg.edu.ntu.simple_crm.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Creates and validates JSON Web Tokens (JWTs) used to identify authenticated
 * users.
 *
 * <p>
 * The token contains a username, its creation time, and its expiration time. It
 * is
 * signed with a server-side secret so that changes to the token can be
 * detected.
 * </p>
 */
@Service
public class JwtService {

    // Spring injects this value from the jwt.secret configuration property.
    // For HS256, the secret must be at least 256 bits (32 bytes) long.
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Token lifetime in milliseconds, supplied by jwt.expiration-ms.
    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    /**
     * Converts the configured secret into the cryptographic key used by HS256.
     * The same key is required both when signing and when verifying a token.
     */
    private Key getSigningKey() {
        // UTF-8 gives a predictable byte representation across environments.
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a signed token for the supplied username.
     *
     * @param username the authenticated user's unique name
     * @return a compact JWT suitable for an Authorization header
     */
    public String generateToken(String username) {
        // JWT timestamps are represented by Date objects in JJWT 0.11.x.
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username) // The "sub" claim identifies the user.
                .setIssuedAt(now) // The "iat" claim records when the token was created.
                .setExpiration(expiry) // The "exp" claim limits how long it is accepted.
                // Signing protects the header and payload from undetected modification.
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                // compact() serializes the header, claims, and signature into x.y.z form.
                .compact();
    }

    /**
     * Verifies a token and reads the username stored in its subject claim.
     *
     * @param token the compact JWT received from a client
     * @return the username stored in the token
     * @throws io.jsonwebtoken.JwtException if the token is invalid or expired
     */
    public String extractUsername(String token) {
        // Parsing verifies the signature and standard claims, including expiration.
        // Invalid, expired, malformed, or unsupported tokens cause an exception.
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                // The body contains the claims only after verification succeeds.
                .getBody();

        return claims.getSubject();
    }

    /**
     * Determines whether a token is well formed, correctly signed, and unexpired.
     *
     * @param token the compact JWT to verify
     * @return {@code true} when parsing and verification succeed; otherwise
     *         {@code false}
     */
    public boolean isTokenValid(String token) {
        try {
            // No claims need to be read here; successful parsing is enough to prove
            // validity.
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception ex) {
            // Authentication callers receive a simple false instead of a JWT exception.
            // A production service may log the exception category without logging the
            // token.
            return false;
        }
    }
}