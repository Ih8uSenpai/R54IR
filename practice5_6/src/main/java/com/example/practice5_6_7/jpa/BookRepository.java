package com.example.practice5_6_7.jpa;

import com.example.practice5_6_7.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {}