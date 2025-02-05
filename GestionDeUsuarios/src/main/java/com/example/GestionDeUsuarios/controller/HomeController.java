package com.example.GestionDeUsuarios.controller;

import com.example.GestionDeUsuarios.entitys.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*
* Controlador para las rutas HTMNL
* */
@Controller
public class HomeController {

    @GetMapping("/loginForm")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/home")
    public String homePage(){
        return "home";
    }
}
