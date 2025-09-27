package com.example.demo.security;

import com.example.demo.enums.Role;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SignatureException;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "my_super_secure_secret_key_12345678901234567890"; // Min 32 chars
    private final String REFRESH_SECRET = "my_super_secure_refresh_key_123456789012345";
    private static final long EXPIRATION = 1000 * 60 * 60 * 10; // 15 minutes
    private final long REFRESH_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7 days

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
    private Key getRefreshKey() {
        return Keys.hmacShaKeyFor(REFRESH_SECRET.getBytes());
    }

    public String generateToken(String email , Role role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role" , role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignKey())
                .compact();
    }

    public String generateRefreshToken(String email , Role role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role" , role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(getRefreshKey())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractUsernameFromRefresh(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getRefreshKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getRefreshKey()) // FIXED: Use refresh key
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Refresh token expired: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Invalid refresh token: " + e.getMessage());
            return false;
        }
    }

}
