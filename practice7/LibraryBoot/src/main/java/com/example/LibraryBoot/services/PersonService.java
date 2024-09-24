package com.example.LibraryBoot.services;


import com.example.LibraryBoot.models.Book;
import com.example.LibraryBoot.models.Person;
import com.example.LibraryBoot.repositories.BookRepository;
import com.example.LibraryBoot.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;
    private final BookRepository bookRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, BookRepository bookRepository) {
        this.personRepository = personRepository;
        this.bookRepository = bookRepository;
    }

    public List<Person> findAll(){
        return personRepository.findAll();
    }

    public Person findById(int id){

        return personRepository.findById(id).orElse(null);
    }
    public Person setLater(int id){
        Person person = personRepository.findById(id).orElse(null);
        for (Book book : person.getBooks()){
            Date date1 = new Date();
            Date date2 = book.getTakenAt();
            if (Math.abs(date2.getTime() - date1.getTime()) > 864000000){
                book.setLater(true);
            }else{
                book.setLater(false);
            }
        }
        return person;
    }
    @Transactional
    public void save(Person person){
        personRepository.save(person);
    }
    @Transactional
    public void update(Person person, int id){
        Person person1 = personRepository.findById(id).orElse(null);
        person1.setName(person.getName());
        person1.setYearOfBirth(person.getYearOfBirth());
        // person1.setBooks(person.getBooks());
        personRepository.save(person1);
    }
    @Transactional
    public void delete(int id){
        personRepository.delete(personRepository.findById(id).orElse(null));
    }
    public Person findByName(String name){
        return personRepository.findByName(name);
    }
}
