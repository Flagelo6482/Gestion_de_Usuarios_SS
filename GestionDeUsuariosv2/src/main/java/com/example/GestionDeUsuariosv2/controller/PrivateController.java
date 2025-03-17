package com.example.GestionDeUsuariosv2.controller;

import com.example.GestionDeUsuariosv2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/*
 * Controlador para un ADMINISTRADOR que necesita hacer operaciones criticas como eliminar, modificar datos sensibles
 * de otros usuarios, etc. Operaciones que no tendría un usuario normal con el rol USER
 * */
@Controller
@RequestMapping("/private")
public class PrivateController {


    //Página privada que iremos cuando iniciemos sesión
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        // Obtener el nombre de usuario del contexto de seguridad
        String username = authentication.getName();
        model.addAttribute("username", username);
        return "dashboard";
    }


}
