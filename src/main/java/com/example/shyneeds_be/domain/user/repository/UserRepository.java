package com.example.shyneeds_be.domain.user.repository;

import com.example.shyneeds_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User, Long> {
}