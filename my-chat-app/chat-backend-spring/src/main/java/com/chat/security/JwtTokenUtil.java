package com.chat.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private String secretKey = "your_secret_key";  // Replace with a more secure key

    // Generate JWT token
    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // 10 hours expiration
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Extract user ID from the token
    public Long extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()   // Use `parserBuilder()` instead of `parseClaimsJws()`
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    // Validate the token
    public boolean isTokenExpired(String token) {
        Claims claims = Jwts.parserBuilder()  // Use `parserBuilder()` for compatibility
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().before(new Date());
    }

    // Validate the token and return if it's valid
    public boolean validateToken(String token, Long userId) {
        return (userId.equals(extractUserId(token)) && !isTokenExpired(token));
    }
}
