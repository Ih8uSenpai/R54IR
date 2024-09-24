package com.example.practice5_6_7.entities;

import com.example.practice5_6_7.util.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // Добавлено поле для роли

    public Client() {
        this.role = Role.USER;
    }
}