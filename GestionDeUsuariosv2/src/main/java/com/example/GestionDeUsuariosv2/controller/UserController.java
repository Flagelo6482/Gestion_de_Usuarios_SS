package com.example.GestionDeUsuariosv2.controller;

import com.example.GestionDeUsuariosv2.entity.UserImpl;
import com.example.GestionDeUsuariosv2.service.RolService;
import com.example.GestionDeUsuariosv2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/api/usuarios")    //Prefijo de las rutas
public class UserController {

    @Autowired
    private UserService userService;

    //Obtenemos todos los usuarios - METODO YA PROBADO EXITOSO!
    @GetMapping
    public List<UserImpl> obtenersTodosLosUsuarios(){
        return userService.listarUsuarios();
    }

    //Obtener un usuario por ID - METODO YA PROBADO EXITOSO!
    @GetMapping("/{id}")
    public ResponseEntity<UserImpl> obtenerUsuarioPorId(@PathVariable Long id){
        return userService.obtenerUsuarioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Crear un nuevo usuario - METODO YA PROBADO EXITOSO!
    @PostMapping("/create")
    public ResponseEntity<UserImpl> crearUsuario(@RequestBody UserImpl user){
        UserImpl nuevoUsuario = userService.crearUnUsuario(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    //Actualizar un usuario - METODO YA PROBADO EXITOSO!
    @PutMapping("/{id}")
    public ResponseEntity<UserImpl> actualizarUsuario(@PathVariable Long id, @RequestBody UserImpl userUpdated){
        return ResponseEntity.ok(userService.actualizarUsuario(id, userUpdated));
    }

    //Eliminar usuario - METODO YA PROBADO EXITOSO!
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id){
        userService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    //Método para iniciar sesión en el proyecto
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserImpl user){
        return userService.verify(user);
    }
}
