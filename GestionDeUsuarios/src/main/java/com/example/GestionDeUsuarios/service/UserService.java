package com.example.GestionDeUsuarios.service;

import com.example.GestionDeUsuarios.entitys.UserImpl;
import com.example.GestionDeUsuarios.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    /*
    * Aplicamos inyección de dependencias
    * */
//    @Autowired
//    private UserRepository userRepository;

    /*
    * Encriptamos la contraseña
    * Con el parametro "12" indicamos que se hashee 12 veces(mientras más mejor >:D)
    * */
//    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);



    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /*
    * Agregamos el constructor, este constructor inyecta el UserRepository y PasswordEncoder como dependencias(como si fuera inyección dependencias)
    * */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    //Creación de un usuario
    public UserImpl addUser(UserImpl user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    //Obtener todos los usuarios
    public List<UserImpl> getAllUsers(){
        return userRepository.findAll();
    }
}
