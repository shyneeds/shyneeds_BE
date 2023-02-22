package com.example.shyneeds_be.global.security.oauth.service;

import com.example.shyneeds_be.domain.user.model.entity.Authority;
import com.example.shyneeds_be.domain.user.model.entity.User;
import com.example.shyneeds_be.domain.user.repository.UserRepository;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import com.example.shyneeds_be.global.security.jwt.TokenProvider;
import com.example.shyneeds_be.global.security.jwt.dto.TokenDto;
import com.example.shyneeds_be.global.security.oauth.dto.AuthRequestDto;
import com.example.shyneeds_be.global.security.oauth.dto.AuthResponseDto;
import com.example.shyneeds_be.global.security.oauth.dto.LoginRequestDto;
import com.example.shyneeds_be.global.security.oauth.dto.SignupRequestDto;
import com.example.shyneeds_be.global.security.oauth.dto.request.ValidateRefreshRequestDto;
import com.example.shyneeds_be.global.security.oauth.dto.response.RecreatedAccessTokenResponseDto;
import com.example.shyneeds_be.global.security.oauth.oauth.ClientKakao;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final ClientKakao clientKakao;
    private final RedisTemplate<String, String> redisTemplate;

    /*
        일반 로그인
    */
    @Transactional
    public ApiResponseDto login(LoginRequestDto loginRequestDto){
        try {
            // Login ID/PW 기반으로 AuthenticationToken 생성
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

            // 검증 (사용자 비밀번호 체크)
            // authenticate 메서드 실행 -> CustomUserDetailService에서 만든 loadUserByUsername 메서드 실행
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // 인증 정보를 기반 JWT 토큰 생성 문제
            TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

            // redis에 refreshToken 저장하는것 필요
            redisTemplate.opsForValue().set(
                    authentication.getName(),
                    tokenDto.getRefreshToken()
            );

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "로그인에 성공했습니다", tokenDto);
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
            if(userRepository.findByEmail(signupRequestDto.getEmail()).isPresent()){
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
                    .authority(Authority.ROLE_USER)
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

            if (userRepository.findByKakaoId(kakaoId) == null){
                User user = User.builder()
                        .kakaoId(kakaoUser.getKakaoId())
                        .email(kakaoUser.getEmail())
                        .name(kakaoUser.getName())
                        .gender(kakaoUser.getGender())
                        .authority(Authority.ROLE_USER)
                        .totalPaymentAmount(0L)
                        .build();

                userRepository.save(user);

            }

            User user = userRepository.findByKakaoId(kakaoId);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

            // redis에 refreshToken 저장하는것 필요
            redisTemplate.opsForValue().set(
                    authentication.getName(),
                    tokenDto.getRefreshToken()
            );

        return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "카카오 로그인에 성공했습니다. " + tokenDto);

        }catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "카카오 로그인에 실패했습니다 " + e.getMessage());
        }
    }

    /*
        refreshToken 이용 accessToken 재발급 이거할차례
    */
    @Transactional
    public ApiResponseDto validateRefreshToken(ValidateRefreshRequestDto validateRefreshRequest) {
        try{
            // refresh token 검증
            if (!tokenProvider.validateToken(validateRefreshRequest.getRefreshToken())) {
                throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
            }

            // AccessToken에서 MemberId 가져오기
            Authentication authentication = tokenProvider.getAuthentication(validateRefreshRequest.getAccessToken());

            // 저장소에서 MemberId 기반으로 Refresh token 값 가져옴
            String refreshToken = redisTemplate.opsForValue().get(authentication.getName());

            // Refresh token이 일치하는지 검사
            if (!refreshToken.equals(validateRefreshRequest.getRefreshToken())) {
                throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
            }

            // 새로운 토큰 생성
            TokenDto newToken = tokenProvider.generateTokenDto(authentication);

            // 저장소 정보 업데이트
            redisTemplate.opsForValue().set(
                    authentication.getName(),
                    newToken.getRefreshToken()
            );

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "새로운 Access token 발급 성공", newToken);
        } catch (ExpiredJwtException e){
            return ApiResponseDto.of(ResponseStatusCode.UNAUTHORIZED.getValue(), "Refresh Token의 기한이 만료되었습니다. 다시 로그인 해주세요.");
        }
        catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "refresh token 검증에 실패했습니다." + e.getMessage());
        }
    }

    public User getUserIdByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}