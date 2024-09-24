package com.example.practice5_6_7.services;

import com.example.practice5_6_7.entities.Client;
import com.example.practice5_6_7.jpa.ClientRepository;
import com.example.practice5_6_7.util.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Client client = clientRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUser(client.getUsername(), client.getPassword(), new ArrayList<>(), client.getId());
    }
}