package com.example.GestionDeUsuariosv2.controller;

import com.example.GestionDeUsuariosv2.entity.UserImpl;
import com.example.GestionDeUsuariosv2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
* Controlador para un ADMINISTRADOR que necesita hacer operaciones criticas como eliminar, modificar datos sensibles
* de otros usuarios, etc. Operaciones que no tendría un usuario normal con el rol USER
* */
@Controller
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService service;

    //Ruta para obtener todos los usuarios
    @GetMapping
    public List<UserImpl> obtenersTodosLosUsuarios(){
        return service.listarUsuarios();
    }

    //Ruta para crear un usuario
    @PostMapping("/create")
    public ResponseEntity<UserImpl> crearUsuario(@RequestBody UserImpl user){
        UserImpl nuevoUsuario = service.crearUnUsuario(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }
}
