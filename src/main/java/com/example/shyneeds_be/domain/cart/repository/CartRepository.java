package com.example.shyneeds_be.domain.cart.repository;

import com.example.shyneeds_be.domain.cart.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByMemberId(Long id);
}
