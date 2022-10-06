package com.example.shyneeds_be.domain.user.repository;

import com.example.shyneeds_be.domain.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User, Long> {
    User findByEmail(String email);

    User findByKakaoId(Long kakaoId);
}
