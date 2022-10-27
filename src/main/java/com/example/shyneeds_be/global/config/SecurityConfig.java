package com.example.shyneeds_be.global.config;

import com.example.shyneeds_be.global.auth.jwt.AuthTokenProvider;
import com.example.shyneeds_be.global.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthTokenProvider authTokenProvider;

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

                .addFilterBefore(new JwtAuthenticationFilter(authTokenProvider), UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
