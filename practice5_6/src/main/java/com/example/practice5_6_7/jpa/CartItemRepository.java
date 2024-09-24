package com.example.practice5_6_7.jpa;

import com.example.practice5_6_7.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {}