package com.example.shyneeds_be.global.auth.jwt;

import com.example.shyneeds_be.global.auth.dto.TokenInfoDto;
import io.jsonwebtoken.*;
import lombok.*;
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

    public AuthToken(String email, String role, Date accessExpiry,Date refreshExpiry, Key key) {
        this.key = key;
        this.token = createAuthToken(email, role, accessExpiry, refreshExpiry);
    }

    private TokenInfoDto createAuthToken(String email, String role, Date accessExpiry, Date refreshExpiry) {

        String accessToken = Jwts.builder()
                .setSubject(email)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(accessExpiry)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(refreshExpiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfoDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
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
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

}
