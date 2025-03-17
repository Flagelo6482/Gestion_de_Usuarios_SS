package com.example.GestionDeUsuariosv2.controller;

import com.example.GestionDeUsuariosv2.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private AuthService authService;

    //Página pública e inicial del proyecto
    @GetMapping("/")
    public String index(){
        return "index";
    }

    //Página para iniciar sesión en el proyecto
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }
        return "login";
    }

    //Página para crear un usuario y guardarlo en la base de datos
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    //Metodo para procesar el formulario del login
    @PostMapping("/auth/login")
    public String processLogin(@RequestParam String username, @RequestParam String password, HttpSession session) {
        String result = authService.verify(username, password);
        if (result.equals("redirect:/private/dashboard")) {
            session.setAttribute("username", username); // Guardar el nombre de usuario en la sesión
        }
        return result;
    }
}