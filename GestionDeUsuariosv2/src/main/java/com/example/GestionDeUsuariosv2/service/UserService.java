package com.example.GestionDeUsuariosv2.service;

import com.example.GestionDeUsuariosv2.dto.AuthResponse;
import com.example.GestionDeUsuariosv2.entity.Rol;
import com.example.GestionDeUsuariosv2.entity.RolName;
import com.example.GestionDeUsuariosv2.entity.UserDetailsImpl;
import com.example.GestionDeUsuariosv2.entity.UserImpl;
import com.example.GestionDeUsuariosv2.reposistory.RolRepository;
import com.example.GestionDeUsuariosv2.reposistory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolRepository rolRepository;  // 🔥 Inyección de RolService
    @Autowired
    private RolService rolService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private JWTService jwtService;

    /*
    * Método para buscar un usuario por el "email"
    * */
    public Optional<UserImpl> obtenerUsuerioPorEmail(String email){
        return userRepository.findByEmail(email);
    }

    /*
    * Método para actualizar los datos de un usuario
    * */
    public UserImpl actualizarUsuario(Long id, UserImpl userUpdated){
        return userRepository.findById(id).map(user -> {
            user.setUsername(userUpdated.getUsername());
            user.setEmail(userUpdated.getEmail());

            //Verificamos si la nueva contraseña es diferente antes de encriptarla
            if (!userUpdated.getPassword().equals(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(userUpdated.getPassword()));
            }
            return userRepository.save(user); // ✅ Esto actualiza el usuario correctamente
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    //Crear un usuario
    public UserImpl crearUnUsuario(UserImpl user){

        /*
        * Aseguramos que la colección de roles este inicializada, en caso venga como null el valor de los roles
        * inicializamos en el constructor de la entidad o dentro del método
        * */
        if(user.getRoles() == null){
            user.setRoles(new HashSet<>());
        }

        /*
        * Verificamos si se enviaron los roles en el request/solocitud
        * Que no este vacio y que sea falso al llamar "isEmpty"
        * */
        if(user.getRoles() != null && !user.getRoles().isEmpty()){

            Set<Rol> persistedRoles = new HashSet<>();

            //Iteramos en los roles del usuario que enviamos con los datos
            for(Rol role : user.getRoles()){
                //Convertir el valor recibido a "enum" (para asegurar que coincida con el enum definido)
                RolName rolName = role.getName();

                //Buscamos el rol persistente en la base de datos
                Rol persistedRole = rolRepository.findByName(rolName)
                        .orElseThrow(() -> new RuntimeException("Rol "+rolName+" no encontrado."));

                persistedRoles.add(persistedRole);
            }
            user.setRoles(persistedRoles);
        } else {
            //Si no enviamos ningun ROL, asignamos un rol por defecto
            Rol defaultRole = rolRepository.findByName(RolName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Rol predeterminado ROL_USER no encontrado"));
            user.getRoles().add(defaultRole);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    //Obtener un usuario por ID
    public Optional<UserImpl> obtenerUsuarioPorId(Long id){
        return userRepository.findById(id);
    }

    //Obtener una lista de todos los usuarios
    public List<UserImpl> listarUsuarios(){
        return userRepository.findAll();
    }

    //Eliminar un usuario por ID
    public void eliminarUsuario(Long id){
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario con id:"+id+" no existe.");
        }
        userRepository.deleteById(id);
    }

    //Verificar si un usuario existe por ID
    public boolean existeUsuario(Long id){
        return userRepository.existsById(id);
    }

    //✅ Método para crear un usuario y asignarle un rol
//    public UserImpl crearUsuarioConRol(UserImpl user, String nombreDeRol){
//        Rol rol = rolService.obtenerRolPorNombre(nombreDeRol)
//                .orElseThrow(() -> new RuntimeException("Rol no encontrado D:"));
//
//        // 🔥 Agregar el rol al Set de roles
//        user.getRoles().add(rol);
//        return userRepository.save(user);
//    }
}
