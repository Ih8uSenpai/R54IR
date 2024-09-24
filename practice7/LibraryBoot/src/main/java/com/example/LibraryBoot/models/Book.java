package com.example.LibraryBoot.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;


import java.util.Date;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "title")
    @NotEmpty(message = "field must not be empty")
    private String title;
    @Column(name = "author")
    @NotEmpty(message = "field must not be empty")
    @Pattern(regexp = "[A-Z][a-z]+ [A-Z][a-z]+ [A-Z][a-z]+",
            message = "Full name should consist of 3 words with a capital letter")
    private String author;
    @Column(name = "year_of_creation")
    @Min(value = 1000, message = "year must be greater then 1000")
    private int yearOfCreation;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;
    @Column(name = "taken_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenAt;


    public Book() {
    }

    public Book(String title, String author, int yearOfCreation) {
        this.title = title;
        this.author = author;
        this.yearOfCreation = yearOfCreation;
        takenAt = new Date();
    }
    @Transient
    private boolean isLater;

    public boolean isLater() {
        return isLater;
    }

    public void setLater(boolean later) {
        isLater = later;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfCreation() {
        return yearOfCreation;
    }

    public void setYearOfCreation(int yearOfCreation) {
        this.yearOfCreation = yearOfCreation;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Date getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Date takenAt) {
        this.takenAt = takenAt;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", yearOfCreation=" + yearOfCreation +
                '}';
    }
}
