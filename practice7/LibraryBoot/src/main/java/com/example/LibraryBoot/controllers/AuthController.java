package com.example.LibraryBoot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;



@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String getLogin(){
        return "form";
    }
    @GetMapping("/reg")
    public String getReg(){
        return "reg";
    }
    @GetMapping("/regReader")
    public String getRegReader(){
        return "regReader";
    }
    @GetMapping("/regAdmin")
    public String getRegAdmin(){
        return "regAdmin";
    }


    @GetMapping("/test")
    public RedirectView login() {
        return new RedirectView("http://localhost:8081/auth/hi");
    }
}
