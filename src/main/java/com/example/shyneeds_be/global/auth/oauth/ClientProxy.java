package com.example.shyneeds_be.global.auth.oauth;

import com.example.shyneeds_be.domain.user.model.entity.User;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

public interface ClientProxy {
    User getUserData(String accessToken);
}
