package com.example.shyneeds_be.domain.cart.repository;

import com.example.shyneeds_be.domain.cart.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
