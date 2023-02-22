package com.example.shyneeds_be.global.security.oauth.oauth;

import com.example.shyneeds_be.domain.user.model.entity.User;

public interface ClientProxy {
    User getUserData(String accessToken);
}
