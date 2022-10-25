package com.example.shyneeds_be.global.auth.jwt;

import com.example.shyneeds_be.global.auth.dto.TokenInfoDto;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.sql.Date;

@Slf4j
@Getter
@AllArgsConstructor
public class AuthToken {

    private final TokenInfoDto token;

    private Key key;

    private static final String AUTHORITIES_KEY = "auth";

    public AuthToken(String email, Long userId, String role, Date accessExpiry,Date refreshExpiry, Key key) {
        this.key = key;
        this.token = createAuthToken(email, userId, role, accessExpiry, refreshExpiry);
    }

    public AuthToken(String email, Long userId, String role, Date accessExpiry, Key key){
        this.key = key;
        this.token = recreateAccessToken(email, userId, role, accessExpiry);
    }

    private TokenInfoDto recreateAccessToken(String email, Long userId, String role, Date accessExpiry) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("userId", userId);
        claims.put("role", role);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(accessExpiry)
                .compact();

        return TokenInfoDto.builder()
                .accessToken(accessToken)
                .keyEmail(email)
                .build();
    }

    private TokenInfoDto createAuthToken(String email, Long userId, String role, Date accessExpiry, Date refreshExpiry) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("userId", userId);
        claims.put("role", role);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(accessExpiry)
                .compact();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(refreshExpiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfoDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .keyEmail(email)
                .build();
    }

    public boolean validate() {
        return this.getTokenClaims() != null;
    }

    public Claims getTokenClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.getAccessToken())
                    .getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        } catch (Exception e){
            log.error("Exception ERROR: {}", e.getMessage());
        }
        return null;
    }
}
