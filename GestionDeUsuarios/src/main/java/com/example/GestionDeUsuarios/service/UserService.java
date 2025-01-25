package com.example.GestionDeUsuarios.service;

import com.example.GestionDeUsuarios.entitys.UserImpl;
import com.example.GestionDeUsuarios.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    /*
    * Aplicamos inyección de dependencias
    * */
    @Autowired
    private UserRepository userRepository;


    //Creación de un usuario
    public UserImpl addUser(UserImpl user){
        return userRepository.save(user);
    }

    //Obtener todos los usuarios
    public List<UserImpl> getAllUsers(){
        return userRepository.findAll();
    }
}
