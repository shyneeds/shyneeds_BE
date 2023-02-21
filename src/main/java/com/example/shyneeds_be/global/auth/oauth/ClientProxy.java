package com.example.shyneeds_be.global.auth.oauth;

import com.example.shyneeds_be.domain.user.model.entity.User;

public interface ClientProxy {
    User getUserData(String accessToken);
}
