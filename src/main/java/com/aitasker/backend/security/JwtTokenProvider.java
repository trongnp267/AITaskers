package com.aitasker.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;
@SuppressWarnings("deprecation")
@Component
public class JwtTokenProvider {
    private final String SECRET_KEY = "your-very-secure-secret-key-that-must-be-long-enough";
    private final long EXPIRATION = 86400000L;
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    public String getUsernameFromToken(String token) {
    return Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
}
public boolean validateToken(String token) {
    try {
        Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
        return true;
    } catch (Exception e) {
        return false;
    }
}
}