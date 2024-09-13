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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.US_ASCII;

@Component
public class JwtService {

    private final String cookieName;
    private final int accessTokenExpiryMinutes;
    private final int refreshTokenExpiryMinutes;
    private final SecretKey key;

    public JwtService(@Value("${city.manager.jwt.access.token.cookie.name}") String cookieName,
                      @Value("${city.manager.jwt.access.token.expiry.minutes}") int accessTokenExpiryMinutes,
                      @Value("${city.manager.jwt.refresh.token.expiry.minutes}") int refreshTokenExpiryMinutes,
                      @Value("${city.manager.jwt.secret}") String secret) {
        this.cookieName = cookieName;
        this.accessTokenExpiryMinutes = accessTokenExpiryMinutes;
        this.refreshTokenExpiryMinutes = refreshTokenExpiryMinutes;
        this.key = Keys.hmacShaKeyFor(secret.getBytes(US_ASCII));
    }

    public ResponseCookie generateAccessCookie(String email) {
        String accessToken = generateJwtToken(email, accessTokenExpiryMinutes);
        return generateCookie(accessToken, accessTokenExpiryMinutes);
    }

    public String generateRefreshToken(String email) {
        return generateJwtToken(email, refreshTokenExpiryMinutes);
    }

    public Optional<String> getEmail(HttpServletRequest request) {
        Optional<String> jwt = getJwtCookie(request);
        return jwt.map(this::getEmailFromJwt);
    }

    public String getEmailFromJwt(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

    public ResponseCookie generateClearAccessCookie() {
        return generateCookie(null, 0);
    }

    private ResponseCookie generateCookie(String jwt, int expiryMinutes) {
        return ResponseCookie.from(cookieName, jwt)
                .path("/api")
                .maxAge(Duration.ofMinutes(expiryMinutes))
                .httpOnly(true)
                .build();
    }

    private String generateJwtToken(String email, int expiryMinutes) {
        Date issuedAt = new Date();
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(issuedAt)
                .setExpiration(getExpiryDate(issuedAt, expiryMinutes))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Date getExpiryDate(Date issuedAt, int expiryMinutes) {
        Instant expiryInstant = issuedAt.toInstant().plus(expiryMinutes, ChronoUnit.MINUTES);
        return Date.from(expiryInstant);
    }

    private Optional<String> getJwtCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        if (cookie != null) {
            return Optional.of(cookie.getValue());
        }
        return Optional.empty();
    }
}
