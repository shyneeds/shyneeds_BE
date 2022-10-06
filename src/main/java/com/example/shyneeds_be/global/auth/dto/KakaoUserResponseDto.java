package com.example.shyneeds_be.global.auth.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoProfile {

    public Long id;
    public Properties properties;
    public KakaoAccount kakao_account;

    @ToString
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public class Properties {
        public String nickname;
    }

    @ToString
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public class KakaoAccount {
        public String email;
        public String birthday;
        public String gender;

    }

}