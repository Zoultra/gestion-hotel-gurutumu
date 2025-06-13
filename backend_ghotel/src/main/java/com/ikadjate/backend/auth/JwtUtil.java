package com.ikadjate.backend.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.ikadjate.backend.model.Role;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey = Keys.hmacShaKeyFor(
            "maSuperCléSecrèteEtSécuriséePourJWT1234567890!".getBytes());
    
    

    public String generateToken(String username, String nom, String prenom, Role role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("nom", nom)
                .claim("prenom", prenom)
                .claim("role", role.getNomrole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject(); // ✅ récupère le username
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // ✅ pas de verifyWith()
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
