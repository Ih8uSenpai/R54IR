package com.example.practice5_6_7.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,      // Использование имени класса
        include = JsonTypeInfo.As.PROPERTY, // Добавление типа как свойства в JSON
        property = "productType"          // Поле, которое будет использоваться для определения типа продукта
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Book.class, name = "Book"),
        @JsonSubTypes.Type(value = Telephone.class, name = "Telephone"),
        @JsonSubTypes.Type(value = WashingMachine.class, name = "WashingMachine")
})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Double price;
    private String sellerNumber;
    private String productType;
    private int stock;
}
