package com.example.shyneeds_be.domain.user.service;

import com.example.shyneeds_be.domain.user.dto.SignupRequestDto;
import com.example.shyneeds_be.domain.user.entity.User;
import com.example.shyneeds_be.domain.user.repository.UserRepository;
import com.example.shyneeds_be.global.auth.dto.AccessTokenResponseDto;
import com.example.shyneeds_be.global.auth.dto.LoginRequestDto;
import com.example.shyneeds_be.global.auth.dto.TokenInfoDto;
import com.example.shyneeds_be.global.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Access;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AccessTokenResponseDto login(LoginRequestDto loginRequestDto){

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenInfoDto tokenInfoDto = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(loginRequestDto.getEmail());
        user.saveRefreshToken(tokenInfoDto.getRefreshToken());
        AccessTokenResponseDto accessTokenResponseDto = new AccessTokenResponseDto(tokenInfoDto.getGrantType(), tokenInfoDto.getAccessToken());

        return accessTokenResponseDto;
    }

    public void signup(SignupRequestDto signupRequestDto) {
        if(userRepository.findByEmail(signupRequestDto.getEmail()) != null){
            throw new RuntimeException("이미 가입된 유저입니다.");
        }

        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        User user = User.builder()
                .email(signupRequestDto.getEmail())
                .password(password)
                .name(signupRequestDto.getName())
                .birthday(signupRequestDto.getBirthday())
                .gender(signupRequestDto.getGender())
                .role("USER")
                .build();

        userRepository.save(user);
    }
}
