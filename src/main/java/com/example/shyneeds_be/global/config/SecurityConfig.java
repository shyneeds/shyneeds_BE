package com.example.shyneeds_be.global.config;

import com.example.shyneeds_be.global.security.jwt.JwtAccessDeniedHandler;
import com.example.shyneeds_be.global.security.jwt.JwtAuthenticationEntryPoint;
import com.example.shyneeds_be.global.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors().configurationSource(
                        request -> {
                            CorsConfiguration cors = new CorsConfiguration();
                            cors.setAllowedOrigins(List.of("*", "http://localhost:3000"));
                            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                            cors.setAllowedHeaders(List.of("*"));

                            return cors;
                        }
                )
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/login","/login/kakao/","/","/signup").permitAll()
                .antMatchers("/api/package/admin/**").hasAnyAuthority("ADMIN")
                .antMatchers("/api/my/user/**").authenticated()
                .antMatchers("/api/reservation/**").authenticated()
                .antMatchers("/api/cart/**").authenticated()
                .antMatchers("/api/review/register", "/api/review/update", "/api/review/mypage").authenticated()
                .antMatchers("/api/comment/register", "/api/comment/{\\d+}",  "/api/comment/update").authenticated()
                .antMatchers("/api/review/like").authenticated()

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
