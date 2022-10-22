package com.example.shyneeds_be.global.auth.jwt;


import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Map;


@RequiredArgsConstructor
@Configuration
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtHeaderUtil jwtHeaderUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception, JwtException {
        boolean hasAnnotation = checkAnnotation(handler, Auth.class);

        if(!hasAnnotation) {
            return true;
        } else{
            try {
                String accessToken = jwtHeaderUtil.getAccessToken(request);
                Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                String value = (String) pathVariables.get("id");

                if (accessToken != null) {
                    final String payloadJWT = accessToken.split("\\.")[1];
                    Base64.Decoder decoder = Base64.getUrlDecoder();

                    final String payload = new String(decoder.decode(payloadJWT));
                    JsonParser jsonParser = new BasicJsonParser();
                    Map<String, Object> jsonArray = jsonParser.parseMap(payload);
                    Long userId = (Long) jsonArray.get("userId");
                    if (Long.valueOf(value) == userId) {
                        return true;
                    } else {
                        return false; // 해당 유저의 토큰이 아닙니다.
                    }
                } else {
                    return false; // 엑세스 토큰이 없습니다.
                }
            } catch (Exception e){
                throw e;
            }
        }
    }

    private boolean checkAnnotation(Object handler, Class<Auth> authClass) {
        if (handler instanceof ResourceHttpRequestHandler){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if(null != handlerMethod.getMethodAnnotation(authClass) || null != handlerMethod.getBeanType().getAnnotation(authClass)){
            return true;
        }
        return false;
    }
}
