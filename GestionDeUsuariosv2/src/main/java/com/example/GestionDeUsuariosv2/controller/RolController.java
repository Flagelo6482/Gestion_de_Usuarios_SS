package com.example.GestionDeUsuariosv2.controller;

import com.example.GestionDeUsuariosv2.entity.Rol;
import com.example.GestionDeUsuariosv2.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    //Obtener todos los roles
    @GetMapping
    public List<Rol> listarRoles(){
        return rolService.listarRoles();
    }

    //Crear un rol
    @PostMapping
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol){
        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.crearRol(rol));
    }

    //Eliminar un ROL
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Long id){
        rolService.eliminarRol(id);
        return ResponseEntity.noContent().build();  // 204 No Content (indica que se eliminó correctamente)
    }
}
