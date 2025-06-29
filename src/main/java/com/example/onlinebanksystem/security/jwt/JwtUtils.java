// src/main/java/com/example/onlinebanksystem/security/jwt/JwtUtils.java
package com.example.onlinebanksystem.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct; // @PostConstruct üçün import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey; // SecretKey üçün import
import java.util.Date;

@Component // Spring-ə bildirir ki, bu sinif bir komponentdir və inject edilə bilər.
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}") // application.properties/yml-dən gizli açarı oxuyur
    private String jwtSecret;

    @Value("${jwt.expiration.ms}") // application.properties/yml-dən tokenin son istifadə müddətini (milisaniyə ilə) oxuyur
    private int jwtExpirationMs;

    private SecretKey key; // JWT imzalamaq üçün istifadə olunacaq SecretKey obyekti

    /**
     * Tətbiq başladıldıqda və bu komponent yaradıldıqda gizli açarı ilkləşdirir.
     * Bu, açarın hər dəfə istifadə olunduqda yenidən dekodlanmasının qarşısını alır
     * və performans artırır.
     */
    @PostConstruct
    public void init() {
        // Base64 kodlanmış gizli açarı byte array-ə çevirir və ondan SecretKey yaradır.
        // HS512 alqoritmi üçün ən azı 512 bit (64 byte) uzunluğunda bir açar tələb olunur.
        // Əgər siz application.yml-də 256 bitlik bir açar istifadə edirsinizsə,
        // bu metod burada 'WeakKeyException' atacaq.
        // Açarın uzunluğunun HS512 üçün kifayət qədər olduğundan əmin olun.
        try {
            this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        } catch (IllegalArgumentException e) {
            logger.error("JWT Secret Key is not valid Base64 or is too short for HS512. Please ensure it's at least 64 characters long and Base64 encoded.");
            throw new RuntimeException("Error initializing JWT Secret Key: " + e.getMessage(), e);
        }
    }


    /**
     * Autentifikasiya edilmiş istifadəçi məlumatlarına əsasən JWT tokeni yaradır.
     *
     * @param authentication Autentifikasiya edilmiş istifadəçi məlumatlarını ehtiva edən obyekt.
     * @return Yeni yaradılmış JWT tokeni string formatında.
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal(); // Autentifikasiya edilmiş istifadəçi məlumatları

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername())) // Tokenin mövzusu (istifadəçi adı/FIN)
                .setIssuedAt(new Date()) // Tokenin yaradılma vaxtı
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Tokenin bitmə vaxtı
                .signWith(this.key, SignatureAlgorithm.HS512) // Hazır ilkləşdirilmiş açar ilə imzalamaq
                .compact(); // Tokeni sıxmaq və sətir olaraq qaytarmaq
    }

    /**
     * Verilmiş JWT tokenindən istifadəçi adını (subjektini) çıxarır.
     *
     * @param token İstiqraz edilən JWT tokeni.
     * @return Tokenin içindəki istifadəçi adı.
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(this.key).build() // Hazır ilkləşdirilmiş açarı istifadə edərək parser qurur
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Verilmiş JWT tokenini doğrulayır.
     *
     * @param authToken Doğrulanacaq JWT tokeni.
     * @return Token etibarlı olarsa `true`, əks halda `false`.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(this.key).build().parse(authToken); // Hazır ilkləşdirilmiş açar ilə tokeni parse edir
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