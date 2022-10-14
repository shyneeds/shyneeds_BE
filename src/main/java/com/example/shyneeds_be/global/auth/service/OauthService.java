package com.example.shyneeds_be.global.auth.service;

import com.example.shyneeds_be.domain.user.model.entity.User;
import com.example.shyneeds_be.domain.user.repository.UserRepository;
import com.example.shyneeds_be.global.auth.dto.AuthRequestDto;
import com.example.shyneeds_be.global.auth.dto.AuthResponseDto;
import com.example.shyneeds_be.global.auth.dto.LoginRequestDto;
import com.example.shyneeds_be.global.auth.dto.SignupRequestDto;
import com.example.shyneeds_be.global.auth.jwt.AuthToken;
import com.example.shyneeds_be.global.auth.jwt.AuthTokenProvider;
import com.example.shyneeds_be.global.auth.oauth.ClientKakao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class OauthService {
    @Value("${kakao.client-id}")
    String clientId;

    @Value("${kakao.client-secret}")
    String clientSecret;

    @Value("${kakao.front-url}")
    String frontUrl;

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final ClientKakao clientKakao;
    private final AuthTokenProvider authTokenProvider;

    @Transactional
    public AuthResponseDto login(LoginRequestDto loginRequestDto){

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        AuthToken token = authTokenProvider.generateToken(authentication, loginRequestDto.getEmail());

        User user = userRepository.findByEmail(loginRequestDto.getEmail());

        user.saveRefreshToken(token.getToken().getRefreshToken());

        return AuthResponseDto.builder()
                .accessToken(token.getToken().getAccessToken())
                .refreshToken(token.getToken().getRefreshToken())
                .userId(user.getId())
                .build();
    }

    @Transactional
    public void signup(SignupRequestDto signupRequestDto){
        if(userRepository.findByEmail(signupRequestDto.getEmail()) != null){
            throw new RuntimeException("이미 가입된 유저입니다.");
        }

        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        Date birthday = null;
        try {
            String strBirthday = signupRequestDto.getYear()+"-"+signupRequestDto.getMonth()+"-"+signupRequestDto.getDay();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            birthday = dateFormat.parse(strBirthday);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        User user = User.builder()
                .email(signupRequestDto.getEmail())
                .password(password)
                .name(signupRequestDto.getName())
                .birthday(birthday)
                .gender(signupRequestDto.getGender())
                .role("USER")
                .build();

        userRepository.save(user);
    }

    @Transactional
    public AuthResponseDto kakaoLogin(AuthRequestDto authRequestDto) {
        User kakaoUser = clientKakao.getUserData(authRequestDto.getAccessToken());
        Long kakaoId = kakaoUser.getKakaoId();
        AuthToken token = null;


        if (userRepository.findByKakaoId(kakaoId) == null){
            token = authTokenProvider.createAppToken(kakaoUser.getEmail(), "USER");
            kakaoUser.builder()
                    .role("USER")
                    .build();

            kakaoUser.saveRefreshToken(token.getToken().getRefreshToken());
            userRepository.save(kakaoUser);

            return AuthResponseDto.builder()
                    .accessToken(token.getToken().getAccessToken())
                    .refreshToken(token.getToken().getRefreshToken())
                    .userId(kakaoUser.getId())
                    .build();

        } else {
            token = authTokenProvider.createToken(kakaoUser.getEmail(), userRepository.findByKakaoId(kakaoId).getRole());
            User user = userRepository.findByKakaoId(kakaoId);
            user.saveRefreshToken(token.getToken().getRefreshToken());
            userRepository.save(user);

            return AuthResponseDto.builder()
                    .accessToken(token.getToken().getAccessToken())
                    .refreshToken(token.getToken().getRefreshToken())
                    .userId(user.getId())
                    .build();
        }
    }

}
