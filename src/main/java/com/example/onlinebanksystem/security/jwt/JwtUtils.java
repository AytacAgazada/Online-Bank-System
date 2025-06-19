// src/main/java/com/example/onlinebanksystem/security/jwt/JwtUtils.java
package com.example.onlinebanksystem.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component // Spring-ə bildirir ki, bu sinif bir komponentdir və inject edilə bilər.
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}") // application.properties-dən gizli açarı oxuyur
    private String jwtSecret;

    @Value("${jwt.expiration.ms}") // application.properties-dən tokenin son istifadə müddətini (milisaniyə ilə) oxuyur
    private int jwtExpirationMs;

    // JWT token yaratmaq üçün metod
    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal(); // Autentifikasiya edilmiş istifadəçi məlumatları

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername())) // Tokenin mövzusu (istifadəçi adı/FIN)
                .setIssuedAt(new Date()) // Tokenin yaradılma vaxtı
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Tokenin bitmə vaxtı
                .signWith(key(), SignatureAlgorithm.HS512) // Gizli açar ilə imzalamaq
                .compact(); // Tokeni sıxmaq və sətir olaraq qaytarmaq
    }

    // İmzalama üçün gizli açarı təmin edən metod
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)); // Base64 kodlanmış açarı decode edir
    }

    // Token içindən istifadəçi adını (subject) çıxarmaq
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // JWT tokeni doğrulamaq üçün metod
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken); // Tokeni açarla parse edir
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}
