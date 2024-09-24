package com.example.practice5_6_7.jpa;

import com.example.practice5_6_7.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
