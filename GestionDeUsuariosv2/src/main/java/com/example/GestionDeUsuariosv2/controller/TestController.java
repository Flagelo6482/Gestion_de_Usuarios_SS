package com.example.GestionDeUsuariosv2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> obtenerRutaProtegida(){
        return ResponseEntity.ok("Ingresaste a una ruta protegida con tus credenciales!");
    }
}
