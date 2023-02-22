package com.example.shyneeds_be.global.security.oauth;

import com.example.shyneeds_be.domain.member.model.entity.Member;

public interface ClientProxy {
    Member getUserData(String accessToken);
}
