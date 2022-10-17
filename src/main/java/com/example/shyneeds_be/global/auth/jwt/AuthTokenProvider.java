package com.example.shyneeds_be.global.auth.jwt;

import com.example.shyneeds_be.global.auth.dto.TokenInfoDto;
import com.example.shyneeds_be.global.exception.TokenValidFailedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuthTokenProvider {

    private Long accessTokenExpireTime = 30*60*1000L;

    private Long refresshTokenExpireTime = 30*24*60*60*1000L;

    private final Key key;

    private static final String AUTHORITIES_KEY = "role";

    public AuthTokenProvider(@Value(" ${jwt.secret-key}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public AuthToken validateRefreshToken(String refreshToken){
        try {
            // 검증
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(refreshToken);

            // refresh 토큰 기간이 만료되지 않았을 경우, 새로운 access 토큰 생성
            if(!claims.getBody().getExpiration().before(new Date(System.currentTimeMillis()))) {
                AuthToken authToken = recreationAccessToken(claims.getBody().get("sub").toString(), claims.getBody().get("role").toString());
                return authToken;
            } else{
                return null;
            }
        } catch (Exception e){
            throw e;
        }
    }

    public AuthToken recreationAccessToken(String email, String role){
        Date accessExpiryDate = getExpiryDate(accessTokenExpireTime);
        return new AuthToken(email, role, accessExpiryDate, key);
    }

    public AuthToken generateToken(Authentication authentication, String email){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date accessExpiryDate = getExpiryDate(accessTokenExpireTime);
        Date refreshExpiryDate = getExpiryDate(refresshTokenExpireTime);

        return new AuthToken(email, authorities, accessExpiryDate, refreshExpiryDate, key);
    }

    public AuthToken createToken(String email, String role) {
        Date accessExpiryDate = getExpiryDate(accessTokenExpireTime);
        Date refreshExpiryDate = getExpiryDate(refresshTokenExpireTime);

        return new AuthToken(email, role, accessExpiryDate, refreshExpiryDate, key);
    }

    public AuthToken createAppToken(String email, String role) {
        if (role == null) {
            return createToken(email, "USER");
        } else{
            return createToken(email, role);
        }
    }

    public AuthToken convertAuthToken(String accessToken) {
        TokenInfoDto token = new TokenInfoDto(accessToken);
        return new AuthToken(token, key);
    }

    public static Date getExpiryDate(Long expiry) {
        return new Date(System.currentTimeMillis() + expiry);
    }

    public Authentication getAuthentication(AuthToken authToken) {

        if(authToken.validate()) {
            Claims claims = authToken.getTokenClaims();
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            User principal = new User(claims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
        } else {
            throw new TokenValidFailedException();
        }
    }


}
