package com.example.shyneeds_be.global.auth.oauth;


import com.example.shyneeds_be.domain.user.model.entity.User;
import com.example.shyneeds_be.global.auth.dto.KakaoUserResponseDto;
import com.example.shyneeds_be.global.exception.TokenValidFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ClientKakao implements ClientProxy {

    private final WebClient webClient;

    @Override
    public User getUserData(String accessToken) {
        KakaoUserResponseDto kakaoUserResponse = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new TokenValidFailedException("Social Access Token is unauthorized")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new TokenValidFailedException("Internal Server Error")))
                .bodyToMono(KakaoUserResponseDto.class)
                .block();

        return User.builder()
                .kakaoId(kakaoUserResponse.getId())
                .name(kakaoUserResponse.getProperties().getNickname())
                .email(kakaoUserResponse.getKakao_account().getEmail())
                .gender(kakaoUserResponse.getKakao_account().getGender())
                .role("USER")
                .build();
    }

}