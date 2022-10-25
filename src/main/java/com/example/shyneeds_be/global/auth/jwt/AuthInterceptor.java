package com.example.shyneeds_be.global.auth.jwt;


import com.example.shyneeds_be.global.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RequiredArgsConstructor
@Configuration
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        boolean hasAnnotation = checkAnnotation(handler, Auth.class);

        if (!hasAnnotation) {
            return true;
        }

        try {
            Long userId = jwtService.getUserId();
            request.setAttribute("userId", userId);
            return true;

        } catch (Exception e) {
            throw e;
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
