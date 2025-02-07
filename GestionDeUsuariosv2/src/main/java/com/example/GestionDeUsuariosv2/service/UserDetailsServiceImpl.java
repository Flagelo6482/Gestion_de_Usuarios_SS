package com.example.GestionDeUsuariosv2.service;

import com.example.GestionDeUsuariosv2.entity.UserDetailsImpl;
import com.example.GestionDeUsuariosv2.entity.UserImpl;
import com.example.GestionDeUsuariosv2.reposistory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
* Clase encargada de obtener las credencialesde un usuario de la base de datos, para esto se implementa "UserDetails"
* para agregar el método "loadUserByUsername" ya que con este método obtendremos los datos que necesitamos validar
* */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    //Llamamos al repositorio para hacer uso del método que encuentra según el nombre de usuario
    @Autowired
    UserRepository repository;


    //Método encargado de buscar y cargar los detalles de un usuario y al implementarlo con una instancia de UserDetails
    //Spring Security lo utiliza para manejar la información del usuario
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Usamos el método del repositorio para buscar el nombre
        UserImpl user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario: "+username+", no encontrado."));

        //Si lo encontramos la entidad base la devolvemos con la entidad que implementa de UserDetails
        return new UserDetailsImpl(user);
    }
}
