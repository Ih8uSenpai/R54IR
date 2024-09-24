package com.example.practice5_6_7.jpa;

import com.example.practice5_6_7.entities.Cart;
import com.example.practice5_6_7.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByClient(Client client);
}