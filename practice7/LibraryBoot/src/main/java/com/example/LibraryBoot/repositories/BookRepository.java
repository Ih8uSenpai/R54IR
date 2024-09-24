package com.example.LibraryBoot.repositories;


import com.example.LibraryBoot.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> getBooksByPersonId(int id);
    List<Book> findBooksByTitleStartingWith(String pattern);
}
