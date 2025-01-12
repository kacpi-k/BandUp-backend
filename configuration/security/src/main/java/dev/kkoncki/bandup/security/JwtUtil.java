package dev.kkoncki.bandup.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {

    private final SecurityProps securityProps;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer";

    private final JwtParser jwtParser;

    public JwtUtil(SecurityProps securityProps) {
        this.securityProps = securityProps;
        this.jwtParser = Jwts.parser().setSigningKey(securityProps.getSecretKey());
    }

    public String generateToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId);
        Instant instant = Instant.now();
        Instant tokenValidity = instant.plus(securityProps.getTokenExpiration(), ChronoUnit.MINUTES);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(tokenValidity))
                .signWith(SignatureAlgorithm.HS256, securityProps.getSecretKey())
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(String token) {
        return parseJwtClaims(token);
    }

    public Optional<String> resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return Optional.of(bearerToken.substring(TOKEN_PREFIX.length()));
        }
        return Optional.empty();
    }

    public boolean validateClaims(Claims claims) {
        return claims.getExpiration().after(new Date());
    }
}
