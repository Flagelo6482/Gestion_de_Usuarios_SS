package com.example.GestionDeUsuarios.service;

import com.example.GestionDeUsuarios.entitys.UserDetailsImpl;
import com.example.GestionDeUsuarios.entitys.UserImpl;
import com.example.GestionDeUsuarios.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
* Clase que se encargara de obtener las credenciales de un usuario que se tiene que implementar "UserDetailsService"
* */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    //Inyectamos la clase que se conecta con la base de datos
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Usamos el método para buscar por nombre
        UserImpl user = userRepository.findByUsername(username);

        //Si no encontramos el usuario
        if (user == null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("user not found....");
        }

        //Creamos un objeto de tipo "UserDetailsImpl"(ya que este se implemente de UserDetails) y asignamos nuestra
        //entidad simple para que la convierta
        return new UserDetailsImpl(user);
    }
}
