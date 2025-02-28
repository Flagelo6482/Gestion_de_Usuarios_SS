package com.example.GestionDeUsuariosv2.entity;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/*
*Clase que envolvera la entidad Base "UserImpl" para que sea manejable en el contexto de SPRING
* */
public class UserDetailsImpl implements UserDetails {

    private final UserImpl user;

    //Constructor que recibe la entidad usuario(user)
    public UserDetailsImpl(UserImpl user) {
        this.user = user;
    }

    public UserImpl getUser() {
        return user;
    }


    //Método para obtener los roles de un USUARIO
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Rol> roles = user.getRoles();
        return roles.stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
