package com.example.practice5_6_7.controllers;

import com.example.practice5_6_7.dto.LoginDto;
import com.example.practice5_6_7.entities.Client;
import com.example.practice5_6_7.jpa.ClientRepository;
import com.example.practice5_6_7.services.JwtTokenService;
import com.example.practice5_6_7.services.UserService;
import com.example.practice5_6_7.util.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Client client) {
        Client registeredUser = userService.registerUser(client);
        String token = jwtTokenService.createToken(registeredUser.getName());

        Map<String, Object> response = new HashMap<>();
        response.put("user", registeredUser);
        response.put("token", token);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticateUser(loginDto.getUsername(), loginDto.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenService.createToken(loginDto.getUsername());
        CustomUser loggedInUser = (CustomUser) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("user", loggedInUser);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(Principal principal) {
        String name = principal.getName();
        System.out.println("sdfdsssssssssssssss\n\n\n\n\n\n\n\n\n\n\n\n username =" + name);
        Client user = clientRepository.findByName(name).orElse(null);
        if (user != null) {
            clientRepository.save(user);
            return ResponseEntity.ok("successful logout");
        }
        return ResponseEntity.ok("user not found");
    }

    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestBody String token) {
        System.out.println("token=" + token);
        try {
            if (jwtTokenService.validateToken(token))
                return ResponseEntity.ok().build();
            else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private Authentication authenticateUser(String username, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }


}
