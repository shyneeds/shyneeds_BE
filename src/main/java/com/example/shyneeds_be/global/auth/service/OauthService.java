package com.example.shyneeds_be.global.auth.service;

import com.example.shyneeds_be.domain.user.model.entity.RefreshToken;
import com.example.shyneeds_be.domain.user.model.entity.User;
import com.example.shyneeds_be.domain.user.repository.RefreshTokenRepository;
import com.example.shyneeds_be.domain.user.repository.UserRepository;
import com.example.shyneeds_be.global.auth.dto.AuthRequestDto;
import com.example.shyneeds_be.global.auth.dto.AuthResponseDto;
import com.example.shyneeds_be.global.auth.dto.LoginRequestDto;
import com.example.shyneeds_be.global.auth.dto.SignupRequestDto;
import com.example.shyneeds_be.global.auth.dto.request.ValidateRefreshRequestDto;
import com.example.shyneeds_be.global.auth.dto.response.RecreatedAccessTokenResponseDto;
import com.example.shyneeds_be.global.auth.jwt.AuthToken;
import com.example.shyneeds_be.global.auth.jwt.AuthTokenProvider;
import com.example.shyneeds_be.global.auth.oauth.ClientKakao;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Ref;
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

    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final ClientKakao clientKakao;
    private final AuthTokenProvider authTokenProvider;

    /*
        일반 로그인
    */
    @Transactional
    public ApiResponseDto<AuthResponseDto> login(LoginRequestDto loginRequestDto){

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            AuthToken token = authTokenProvider.generateToken(authentication, loginRequestDto.getEmail());

            User user = userRepository.findByEmail(loginRequestDto.getEmail());

            if (user.getRefreshToken() != null){
                refreshTokenRepository.delete(user.getRefreshToken());
            }

            RefreshToken refreshToken = RefreshToken.builder()
                    .refreshToken(token.getToken().getRefreshToken())
                    .user(user)
                    .build();
            refreshTokenRepository.save(refreshToken);

            user.saveRefreshToken(refreshToken);
            userRepository.save(user);


            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "로그인에 성공했습니다",
                    AuthResponseDto.builder()
                    .accessToken(token.getToken().getAccessToken())
                    .refreshToken(token.getToken().getRefreshToken())
                    .userId(user.getId())
                    .build());

        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "로그인에 실패했습니다" + e.getMessage());
        }

    }

    /*
        일반 회원가입
    */

    @Transactional
    public ApiResponseDto signup(SignupRequestDto signupRequestDto){
        try{
            if(userRepository.findByEmail(signupRequestDto.getEmail()) != null){
                throw new RuntimeException("이미 가입된 유저입니다.");
            }

            String password = passwordEncoder.encode(signupRequestDto.getPassword());

            Date birthday = null;

            String strBirthday = signupRequestDto.getYear()+"-"+signupRequestDto.getMonth()+"-"+signupRequestDto.getDay();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            birthday = dateFormat.parse(strBirthday);

            User user = User.builder()
                    .email(signupRequestDto.getEmail())
                    .password(password)
                    .name(signupRequestDto.getName())
                    .phoneNumber(signupRequestDto.getPhoneNumber())
                    .birthday(birthday)
                    .gender(signupRequestDto.getGender())
                    .role("USER")
                    .build();

            userRepository.save(user);

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "회원가입에 성공했습니다.");
        } catch (Exception e) {
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "회원가입에 실패했습니다" + e.getMessage());
        }

    }

    /*
        카카오 로그인 및 회원가입
    */
    @Transactional
    public ApiResponseDto<AuthResponseDto> kakaoLogin(AuthRequestDto authRequestDto) {
        try{
            User kakaoUser = clientKakao.getUserData(authRequestDto.getAccessToken());
            Long kakaoId = kakaoUser.getKakaoId();
            AuthToken token = null;
            if (userRepository.findByKakaoId(kakaoId) == null){
                token = authTokenProvider.createAppToken(kakaoUser.getEmail(), "USER");
                kakaoUser.builder()
                        .role("USER")
                        .build();

                RefreshToken refreshToken = RefreshToken.builder()
                        .refreshToken(token.getToken().getRefreshToken())
                        .user(kakaoUser)
                        .build();

                refreshTokenRepository.save(refreshToken);

                kakaoUser.saveRefreshToken(refreshToken);
                userRepository.save(kakaoUser);

                return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(),"카카오 로그인에 성공했습니다",AuthResponseDto.builder()
                        .accessToken(token.getToken().getAccessToken())
                        .refreshToken(token.getToken().getRefreshToken())
                        .userId(kakaoUser.getId())
                        .build());

            } else {
                token = authTokenProvider.createToken(kakaoUser.getEmail(), userRepository.findByKakaoId(kakaoId).getRole());
                User user = userRepository.findByKakaoId(kakaoId);

                if (user.getRefreshToken() != null){
                    refreshTokenRepository.delete(user.getRefreshToken());
                }

                RefreshToken refreshToken = RefreshToken.builder()
                        .refreshToken(token.getToken().getRefreshToken())
                        .user(user)
                        .build();

                refreshTokenRepository.save(refreshToken);

                user.saveRefreshToken(refreshToken);
                userRepository.save(user);

                return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "카카오 로그인에 성공했습니다", AuthResponseDto.builder()
                        .accessToken(token.getToken().getAccessToken())
                        .refreshToken(token.getToken().getRefreshToken())
                        .userId(user.getId())
                        .build());
            }
        }catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "카카오 로그인에 실패했습니다 " + e.getMessage());
        }
    }

    /*
        refreshToken 이용 accessToken 재발급
    */
    public ApiResponseDto<RecreatedAccessTokenResponseDto> validateRefreshToken(Long userId,ValidateRefreshRequestDto validateRefreshRequest) {
        try{
            if(validateRefreshRequest.getRefreshToken().equals(userRepository.findById(userId).get().getRefreshToken().getRefreshToken())) {
                AuthToken createdAccessToken = authTokenProvider.validateRefreshToken(validateRefreshRequest.getRefreshToken());

                return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "새로운 Access token 발급 성공",
                        RecreatedAccessTokenResponseDto.builder()
                                .accessToken(createdAccessToken.getToken().getAccessToken())
                                .build());
            } else{
                return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "refresh token이 일치하지 않습니다.");
            }
        } catch (ExpiredJwtException e){
            return ApiResponseDto.of(ResponseStatusCode.UNAUTHORIZED.getValue(), "Refresh Token의 기한이 만료되었습니다. 다시 로그인 해주세요.");
        }
        catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "refresh token 검증에 실패했습니다." + e.getMessage());
        }
    }
}
