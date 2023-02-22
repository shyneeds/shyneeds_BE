package com.example.shyneeds_be.domain.member.service;

import com.example.shyneeds_be.domain.member.model.dto.request.*;
import com.example.shyneeds_be.domain.member.model.entity.Authority;
import com.example.shyneeds_be.domain.member.model.entity.Member;
import com.example.shyneeds_be.domain.member.repository.MemberRepository;
import com.example.shyneeds_be.global.network.response.ApiResponseDto;
import com.example.shyneeds_be.global.network.response.ResponseStatusCode;
import com.example.shyneeds_be.global.network.s3.ItemS3Uploader;
import com.example.shyneeds_be.global.security.jwt.TokenProvider;
import com.example.shyneeds_be.global.security.jwt.dto.TokenDto;
import com.example.shyneeds_be.global.security.oauth.ClientKakao;
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
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class MemberService {
    @Value("${kakao.client-id}")
    String clientId;

    @Value("${kakao.client-secret}")
    String clientSecret;

    @Value("${kakao.front-url}")
    String frontUrl;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final ClientKakao clientKakao;
    private final RedisTemplate<String, String> redisTemplate;
    private final ItemS3Uploader itemS3Uploader;

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
            if(memberRepository.findByEmail(signupRequestDto.getEmail()).isPresent()){
                throw new RuntimeException("이미 가입된 유저입니다.");
            }

            String password = passwordEncoder.encode(signupRequestDto.getPassword());

            Date birthday = null;

            String strBirthday = signupRequestDto.getYear()+"-"+signupRequestDto.getMonth()+"-"+signupRequestDto.getDay();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            birthday = dateFormat.parse(strBirthday);

            Member member = Member.builder()
                    .email(signupRequestDto.getEmail())
                    .password(password)
                    .name(signupRequestDto.getName())
                    .phoneNumber(signupRequestDto.getPhoneNumber())
                    .birthday(birthday)
                    .gender(signupRequestDto.getGender())
                    .authority(Authority.ROLE_USER)
                    .build();

            memberRepository.save(member);

            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "회원가입에 성공했습니다.");
        } catch (Exception e) {
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "회원가입에 실패했습니다" + e.getMessage());
        }

    }

    /*
        카카오 로그인 및 회원가입
    */
    @Transactional
    public ApiResponseDto kakaoLogin(AuthRequestDto authRequestDto) {
        try{

            Member kakaoMember = clientKakao.getUserData(authRequestDto.getAccessToken());
            Long kakaoId = kakaoMember.getKakaoId();

            if (memberRepository.findByKakaoId(kakaoId) == null){
                Member member = Member.builder()
                        .kakaoId(kakaoMember.getKakaoId())
                        .email(kakaoMember.getEmail())
                        .name(kakaoMember.getName())
                        .gender(kakaoMember.getGender())
                        .authority(Authority.ROLE_USER)
                        .totalPaymentAmount(0L)
                        .build();

                memberRepository.save(member);

            }

            Member member = memberRepository.findByKakaoId(kakaoId);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword());

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

    /*
        유저 정보 수정
    */
    @Transactional
    public ApiResponseDto updateUser(Long id, UpdateMemberRequestDto updateUserRequest, MultipartFile profileImage) {
        try{

            Member member = findMemberById(id);

            String strBirthday = updateUserRequest.getYear() + "-" + updateUserRequest.getMonth() + "-" + updateUserRequest.getDay();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = dateFormat.parse(strBirthday);

            String profileImageUrl = uploadS3MemberProfileImage(profileImage, id.toString());

            if(profileImage != null) {
                if (updateUserRequest.getPassword() != null) {
                    String password = passwordEncoder.encode(updateUserRequest.getPassword());
                    member.updateInfoWithImage(profileImageUrl, password, updateUserRequest.getName(),updateUserRequest.getPhoneNumber() ,birthday, updateUserRequest.getGender());
                }
                member.updateInfoNoPassWithImage(profileImageUrl, updateUserRequest.getName(), updateUserRequest.getPhoneNumber(), birthday, updateUserRequest.getGender());
            } else{
                if (updateUserRequest.getPassword() != null) {
                    String password = passwordEncoder.encode(updateUserRequest.getPassword());
                    member.updateInfo(password, updateUserRequest.getName(), updateUserRequest.getPhoneNumber(), birthday, updateUserRequest.getGender());
                }
                member.updateInfoNoPass(updateUserRequest.getName(), updateUserRequest.getPhoneNumber(), birthday, updateUserRequest.getGender());
            }
            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "사용자 정보 수정에 성공했습니다.");
        } catch (Exception e){
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "사용자 정보 수정에 실패했습니다." + e.getMessage());
        }
    }

    /*
        회원 탈퇴
    */
    public ApiResponseDto deleteUser(Long id) {
        try {
            memberRepository.delete(findMemberById(id));
            return ApiResponseDto.of(ResponseStatusCode.SUCCESS.getValue(), "회원탈퇴를 성공했습니다.");

        } catch (Exception e) {
            return ApiResponseDto.of(ResponseStatusCode.FAIL.getValue(), "회원탈퇴에 실패했습니다.");
        }

    }

    public Member findMemberById(Long id){
        return memberRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("없는 유저입니다.") );
    }

    public String uploadS3MemberProfileImage(MultipartFile profileImage, String bucketDir){
        return itemS3Uploader.uploadLocal(profileImage, bucket+"/user/"+bucketDir);
    }


}
