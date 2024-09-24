package com.example.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private String name;
    private String password;
    private Role role;

    public UserDto(String name, String password, String role) {
        this.name = name;
        this.password = password;
        switch (role){
            case "USER":
                this.role = Role.USER;
                break;
            case "READER":
                this.role = Role.READER;
                break;
            case "ADMIN":
                this.role = Role.ADMIN;
                break;
        }
    }
}
