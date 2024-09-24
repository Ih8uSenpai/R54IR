package com.example.practice5_6_7.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WashingMachine extends Product {
    private String manufacturer;
    private String tankVolume;

    // Геттеры и сеттеры
}

