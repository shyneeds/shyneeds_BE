package com.example.shyneeds_be.global.config;

import com.example.shyneeds_be.global.auth.jwt.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
//                .addPathPatterns("/*") // 해당 경로에 접근하기 전에 인터셉터가 가로챈다.
//                .excludePathPatterns("/"); // 해당 경로는 인터셉터가 가로채지 않는다.;
    }
}
