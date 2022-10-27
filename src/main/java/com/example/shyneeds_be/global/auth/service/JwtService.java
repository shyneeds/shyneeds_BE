package com.example.shyneeds_be.global.auth.service;

import com.example.shyneeds_be.global.auth.jwt.JwtHeaderUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtHeaderUtil jwtHeaderUtil;

    public Long getUserId() throws Exception{

        //1. JWT 추출
        String accessToken = jwtHeaderUtil.getAccessToken(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());

        // 비회원 처리
        if(accessToken == null || accessToken.length() == 0){
//            throw new IllegalAccessException("EMPTY_ACCESS_TOKEN");
            return 0L;
        }
        try {
            final String payloadJWT = accessToken.split("\\.")[1];

            Base64.Decoder decoder = Base64.getUrlDecoder();

            final String payload = new String(decoder.decode(payloadJWT));
            JsonParser jsonParser = new BasicJsonParser();
            Map<String, Object> jsonArray = jsonParser.parseMap(payload);
            return (Long) jsonArray.get("userId");
        } catch (Exception e){
            throw e;
        }
    }
}
