package com.example.shyneeds_be.domain.user.repository;

import com.example.shyneeds_be.domain.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByEmail(String email);

    User findByKakaoId(Long kakaoId);

}
