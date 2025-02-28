package com.example.GestionDeUsuariosv2.controller;

import com.example.GestionDeUsuariosv2.dto.AuthRequest;
import com.example.GestionDeUsuariosv2.dto.AuthResponse;
import com.example.GestionDeUsuariosv2.entity.UserImpl;
import com.example.GestionDeUsuariosv2.service.AuthService;
import com.example.GestionDeUsuariosv2.service.JWTService;
import com.example.GestionDeUsuariosv2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //Utilizamos el RestController ya que no devolveremos un HTML sino un JSON con el token JWT!
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    //ENDPOINT 1: Método que valida credenciales de un usuario y en caso que sea exitoso retorne un token JWT
    @PostMapping("/iniciar")
    public ResponseEntity<?> iniciarSesion(@RequestBody UserImpl user){
        return service.verify(user);
    }
}
