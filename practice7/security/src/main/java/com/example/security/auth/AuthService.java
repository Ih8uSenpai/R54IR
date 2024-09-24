package com.example.security.auth;

import com.example.security.config.JwtService;
import com.example.security.model.Role;
import com.example.security.model.UserDto;
import com.example.security.model.UserImpl;
import com.example.security.repo.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        UserImpl userImpl = UserImpl.builder().name(request.getUsername()).
                password(passwordEncoder.encode(request.getPassword())).role(Role.USER).build();
        userRepository.save(userImpl);
        var jwtToken = jwtService.generateToken(userImpl);
        return AuthenticationResponse.builder().token(jwtToken).role(userImpl.getRole().toString()).
                username(userImpl.getUsername()).build();
    }
    public AuthenticationResponse registerReader(RegisterRequest request) {
        
        UserImpl userImpl = UserImpl.builder().name(request.getUsername()).
                password(passwordEncoder.encode(request.getPassword())).role(Role.READER).build();
        userRepository.save(userImpl);
        var jwtToken = jwtService.generateToken(userImpl);
        return AuthenticationResponse.builder().token(jwtToken).role(userImpl.getRole().toString()).
                username(userImpl.getUsername()).build();
    }
    public AuthenticationResponse registerAdmin(RegisterRequest request) {
        UserImpl userImpl = UserImpl.builder().name(request.getUsername()).
                password(passwordEncoder.encode(request.getPassword())).role(Role.ADMIN).build();
        userRepository.save(userImpl);
        var jwtToken = jwtService.generateToken(userImpl);
        return AuthenticationResponse.builder().token(jwtToken).role(userImpl.getRole().toString()).
                username(userImpl.getUsername()).build();
    }
    public AuthenticationResponse login(RegisterRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDto userDto = userRepository.findUserByUsername(request.getUsername());
        UserImpl user = new UserImpl(userDto.getName(), userDto.getPassword(), userDto.getRole());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).role(user.getRole().toString()).
                username(user.getUsername()).build();
    }
    public boolean checkToken(CheckRequest request){
        UserDto userDto = userRepository.findUserByUsername(request.getUsername());
        UserImpl user = new UserImpl();
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        return jwtService.isTokenValid(request.getToken(), user);
    }



}
