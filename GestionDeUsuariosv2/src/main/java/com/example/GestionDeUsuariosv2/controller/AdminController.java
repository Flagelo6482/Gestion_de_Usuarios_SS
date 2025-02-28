package com.example.GestionDeUsuariosv2.controller;

import com.example.GestionDeUsuariosv2.entity.UserImpl;
import com.example.GestionDeUsuariosv2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Controlador para un ADMINISTRADOR que necesita hacer operaciones criticas como eliminar, modificar datos sensibles
 * de otros usuarios, etc. Operaciones que no tendría un usuario normal con el rol USER
 * */
//@Controller
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService service;

    //Rutas privadas de prueba
    @GetMapping("/ruta1")
    public String rutaDePrueba1(){
        return "Estas en la primer ruta privada";
    }

    @GetMapping("/ruta2")
    public String rutaDePrueba2(){
        return "Estas en la segunda ruta privada";
    }

    @GetMapping("/ruta3")
    public String rutaDePrueba3(){
        return "Estas en la tercer ruta privada";
    }
}
