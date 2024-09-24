package com.example.LibraryBoot.controllers;


import com.example.LibraryBoot.models.Book;
import com.example.LibraryBoot.models.Person;
import com.example.LibraryBoot.services.BookService;
import com.example.LibraryBoot.services.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final PersonService personService;
    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }
    @GetMapping
    public String index(Model model, @RequestParam(name = "page", required = false) Integer page,
                        @RequestParam(name="books_per_page", required = false) Integer perPage,
                        @RequestParam(name="sort_by_year", required = false) Boolean sort){
        model.addAttribute("books", bookService.findAll(page, perPage, sort));
        return "books/index";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        model.addAttribute("people", personService.findAll());
        if (book.getPerson() != null){
            model.addAttribute("concrete_people", personService.findById(book.getPerson().getId()));
        }
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(Model model){
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("book", bookService.findById(id));
        return "books/edit";
    }
    @GetMapping("/search")
    public String search(@RequestParam(name = "pattern", required = false) String pattern, Model model){
        if (pattern != null) {
            model.addAttribute("books", bookService.findStartingWith(pattern));
        }else{
            model.addAttribute("books", new ArrayList<Book>());
        }
        return "books/search";
    }
    @PostMapping
    public String create(@ModelAttribute("book")  @Valid Book book, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "books/new";
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "books/edit";
        }
        bookService.update(book, id);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/set")
    public String set(@PathVariable("id") int bookId, @ModelAttribute("person")Person person){
        bookService.set(bookId, person.getId());
        return "redirect:/books";
    }

    @PatchMapping("/{id}/unset")
    public String unset(@PathVariable("id") int bookId){
        bookService.unset(bookId);
        return "redirect:/books";
    }

}