package com.example.GestionDeUsuariosv2.controller;

import com.example.GestionDeUsuariosv2.entity.UserImpl;
import com.example.GestionDeUsuariosv2.service.RolService;
import com.example.GestionDeUsuariosv2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping("/usuarios")    //Prefijo de las rutas
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
//    @PostMapping("/auth")
//    public ResponseEntity<?> login(@RequestBody UserImpl user){
//        return userService.verify(user);
//    }


    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
        }

        Optional<UserImpl> user = userService.obtenerUsuerioPorEmail(authentication.getName());

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }
}
