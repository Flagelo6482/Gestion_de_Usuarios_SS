package com.example.GestionDeUsuariosv2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String paginaPublica(){
        return "index";     //Renderizamos el index.html
    }

    @GetMapping("/login")
    public String paginaLogin(){
        return "login";
    }

    @GetMapping("/dashboard")
    public String paginaPrivada(){
        return "dashboard";
    }
}
