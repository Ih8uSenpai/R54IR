package com.example.practice5_6_7.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.practice5_6_7.entities.Client;
import com.example.practice5_6_7.jpa.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;

@Service
public class JwtTokenService {
    private final String secretKey = "yourSecretKey";
    @Autowired
    private ClientRepository clientRepository;

    public String createToken(String name) {

        Client client = clientRepository.findByName(name).orElse(null);
        if (client != null) {
            clientRepository.save(client);
            return JWT.create()
                    .withSubject(name)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 36000000))
                    .sign(Algorithm.HMAC512(secretKey));
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            System.out.println("НЕВЕРНЫЙ ТОКЕН" + token);
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        // роли и пермишки можно передавать тут
        return new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }
}
