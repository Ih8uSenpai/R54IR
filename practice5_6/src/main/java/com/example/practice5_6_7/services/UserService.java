package com.example.practice5_6_7.services;

import com.example.practice5_6_7.entities.Client;
import com.example.practice5_6_7.jpa.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class UserService {

    @Autowired
    private ClientRepository clientRepository;




    public Client registerUser(Client client) {
        Client newUser = new Client();
        newUser.setUsername(client.getUsername());
        newUser.setPassword(client.getPassword());
        newUser.setEmail(client.getEmail());



        // Сохранение пользователя
        Client savedUser = clientRepository.save(newUser);




        return savedUser;
    }

}
