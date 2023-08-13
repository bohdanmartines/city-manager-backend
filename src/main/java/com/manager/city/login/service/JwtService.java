package com.manager.city.login.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.US_ASCII;

@Component
public class JwtService {

    private String cookieName;
    private int cookieExpiryMinutes;
    private final SecretKey key;

    public JwtService(@Value("${city.manager.jwt.cookie.name}") String cookieName,
                      @Value("${city.manager.jwt.cookie.exipy.minutes}") int cookieExpiryMinutes,
                      @Value("${city.manager.jwt.cookie.secret}") String secret) {
        this.cookieName = cookieName;
        this.cookieExpiryMinutes = cookieExpiryMinutes;
        this.key = Keys.hmacShaKeyFor(secret.getBytes(US_ASCII));
    }

    public ResponseCookie generateJwtCookie(String email) {
        String jwt = generateJwt(email);
        return generateCookie(jwt, cookieExpiryMinutes);
    }

    public Optional<String> getEmail(HttpServletRequest request) {
        Optional<String> jwt = getJwtCookie(request);
        return jwt.map(this::getEmailFromJwt);
    }

    public ResponseCookie generateClearJwtCookie() {
        return generateCookie(null, 0);
    }

    private ResponseCookie generateCookie(String jwt, int expiryMinutes) {
        return ResponseCookie.from(cookieName, jwt)
                .path("/api")
                .maxAge(Duration.ofMinutes(expiryMinutes))
                .httpOnly(true)
                .build();
    }

    private String generateJwt(String email) {
        Date issuedAt = new Date();
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(issuedAt)
                .setExpiration(getExpiryDate(issuedAt))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Date getExpiryDate(Date issuedAt) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime asLocalDate = issuedAt.toInstant().atZone(zoneId).toLocalDateTime();
        return Date.from(asLocalDate.plusMinutes(cookieExpiryMinutes).atZone(zoneId).toInstant());
    }

    private Optional<String> getJwtCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        if (cookie != null) {
            return Optional.of(cookie.getValue());
        }
        return Optional.empty();
    }

    private String getEmailFromJwt(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }
}
