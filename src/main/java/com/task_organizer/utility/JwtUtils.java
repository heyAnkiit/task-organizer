package com.task_organizer.utility;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;


@Component
@SuppressWarnings("deprecation")
public class JwtUtils {

    private final long expiry = Duration.ofHours(1).toMillis();

    private final Key mySigningKey = Jwts.SIG.HS256.key().build();

    private final String key = "aaaaaa33333333MMMMMMMM111111111111113333333333333333333322222222222ttttttttttttt";

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(mySigningKey ,SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(mySigningKey).build().parseClaimsJws(token).getBody();
    }

    public Boolean validateToken(String token, String identifier) {
        final String username = extractIdentifier(token);
        return (username.equals(identifier) && !isTokenExpired(token));
    }

    public String extractIdentifier(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


}