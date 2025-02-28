package com.example.GestionDeUsuariosv2.controller;

import com.example.GestionDeUsuariosv2.entity.UserImpl;
import com.example.GestionDeUsuariosv2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

//@Controller
@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService service;

    //Página publica para todos los usuarios, donde veremos los usuarios de la base de datos

    //ENDPOINT 1: Paso con exito
    @GetMapping("/inicio")
    public List<UserImpl> publicPage(){
        return service.listarUsuarios();
    }


    //ENDPOINT 2: Retorna correctamente el usuario buscando y el error en caso no exista el correo
    /*
    * Con "@PathVariable" obtenemos el dato de la ruta indicada con "{}"
    * */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable String email){
        return service.obtenerUsuerioPorEmail(email)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok().body(user)) // Se fuerza el tipo ResponseEntity<?>
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado"));
    }

    //ENDPOINT 3: Actualizamos un usuario(a la vez encriptamos la contraseña) en caso el ID no coincida con ningun usuario retorna el error
    /*
    * Con "@RequestBody" RECIBIMOS(en formato JSON) el usuario actualizando en el CUERPO de la solicitud
    * */
    @PostMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarUsuarioPorId(@PathVariable Long id,@RequestBody UserImpl user){
        try {
            UserImpl usuarioActualizado = service.actualizarUsuario(id, user);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado por el id");
        }
    }

    //ENDPOINT 4: Endpoint para crear un usuario, en caso pasemos un rol que no existe nos retornara la respuesta 403
    @PostMapping("/crear")
    public ResponseEntity<?> crearUsuario(@RequestBody UserImpl user){
        try {
            UserImpl nuevoUsuario = service.crearUnUsuario(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //ENDPOINT 5: Endpoint que permite la búsqueda individual de un usuario con su ID correspondiente
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarUsuarioPorId(@PathVariable Long id){
        return service.obtenerUsuarioPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado por el id-"+id));
    }

    //ENDPOINT 6: Endpoint que permite eliminar un usuario por ID
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarUsuarioPorId(@PathVariable Long id){
        try {
            service.eliminarUsuario(id);
            return ResponseEntity.ok("Usuario con id-"+id+" eliminado con exito.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //ENDPOINT 7: Endpoint que verifica si un usuario existe
    @GetMapping("/validar/{id}")
    public ResponseEntity<?> verificarUsuarioPorId(@PathVariable Long id){
        boolean existe = service.existeUsuario(id);

        if(existe){
            return ResponseEntity.ok("Usuario con id:"+id+" encontrado!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con id-" + id + " no existe.");
        }
    }
}
