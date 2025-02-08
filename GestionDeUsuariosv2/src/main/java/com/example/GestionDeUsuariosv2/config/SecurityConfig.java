package com.example.GestionDeUsuariosv2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


/*
* Archivo que gestiona la seguridad y condiciones de las rutas
* */
@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())    //Desactivamos la seguridad CSRF al trabajar con APIs(como POSTMAN)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())   //Todas las rutas se ingresan sin autenticación
                .build();
    }
}
