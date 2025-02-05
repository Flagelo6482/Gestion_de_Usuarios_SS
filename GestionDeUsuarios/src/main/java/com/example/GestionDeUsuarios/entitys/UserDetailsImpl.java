package com.example.GestionDeUsuarios.entitys;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/*
* CLASE QUE SERA DE TIPO "UserDetails" para que Spring Security lo maneje y lo reconozco
* */
public class UserDetailsImpl implements UserDetails {

    //Entidad básica
    private UserImpl user;

    //Constructor para inyectar la entidad que tenemos
    public UserDetailsImpl(UserImpl user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Convertimos el rol del usuario a una autoridad (GrantedAuthority)
        return List.of(new SimpleGrantedAuthority(user.getRol()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    public String getEmail() {   // 🔹 Getter agregado para obtener el email
        return user.getEmail();
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
