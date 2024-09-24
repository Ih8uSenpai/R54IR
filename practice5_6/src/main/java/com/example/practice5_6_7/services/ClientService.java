package com.example.practice5_6_7.services;

import com.example.practice5_6_7.entities.Client;
import com.example.practice5_6_7.exception.ResourceNotFoundException;
import com.example.practice5_6_7.jpa.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Client updateClient(Long id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        
        client.setName(clientDetails.getName());
        client.setEmail(clientDetails.getEmail());
        client.setUsername(clientDetails.getUsername());
        client.setPassword(clientDetails.getPassword());
        // Не меняем роль здесь
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
