package com.example.shyneeds_be.domain.user.repository;

import com.example.shyneeds_be.domain.user.model.entity.RefreshToken;
import com.example.shyneeds_be.domain.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByUser(User user);
}
