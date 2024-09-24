package com.example.LibraryBoot.services;

import com.example.LibraryBoot.models.Book;
import com.example.LibraryBoot.repositories.BookRepository;
import com.example.LibraryBoot.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final PersonRepository personRepository;
    @Autowired
    public BookService(BookRepository bookRepository, PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }
    public List<Book> findAll(Integer page, Integer perPage, Boolean sort){
        if (page != null && perPage != null && sort != null && sort){
            return bookRepository.findAll(PageRequest.of(page, perPage, Sort.by("yearOfCreation"))).
                    getContent();
        }
        if (page != null && perPage != null){
            return bookRepository.findAll(PageRequest.of(page, perPage)).getContent();
        }
        if (sort != null && sort){
            return bookRepository.findAll(Sort.by("yearOfCreation"));
        }
        return bookRepository.findAll();
    }
    public Book findById(int id){
        return bookRepository.findById(id).orElse(null);
    }
    public List<Book> findStartingWith(String pattern){
        return bookRepository.findBooksByTitleStartingWith(pattern);
    }

    @Transactional
    public void save(Book book){
        bookRepository.save(book);
    }
    @Transactional
    public void update(Book book, int id){
        Book book1 = bookRepository.findById(id).orElse(null);
        book1.setAuthor(book.getAuthor());
        book1.setTitle(book.getTitle());
        book1.setYearOfCreation(book.getYearOfCreation());
        bookRepository.save(book1);
    }
    @Transactional
    public void delete(int id){
        bookRepository.delete(bookRepository.findById(id).orElse(null));
    }
    @Transactional
    public void set(int bookId, int personId){
        Book book = bookRepository.findById(bookId).orElse(null);
        book.setTakenAt(new Date());
        book.setPerson(personRepository.findById(personId).orElse(null));
        bookRepository.save(book);
    }
    @Transactional
    public void unset(int bookId){
        Book book = bookRepository.findById(bookId).orElse(null);
        book.setPerson(null);
        bookRepository.save(book);
    }
}