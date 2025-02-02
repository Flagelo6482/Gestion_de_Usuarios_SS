package com.example.GestionDeUsuarios.config;

import com.example.GestionDeUsuarios.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*
    * Definimos el bean en el archivo de seguridad para que Spring lo reconozca
    * */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private JwtFilter jwtFilter;


    //Llamamos a nuestra clase personalizada que implementa UserDetailsService para buscar los datos en la base de datos
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(customizer -> customizer.disable())   //Desactivamos la seguridad CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/createUser")
                        .permitAll()
                        .anyRequest().authenticated())  //Indicamos que todas las rutas no especificadas necesitan ser autenticadas
                .httpBasic(Customizer.withDefaults())   //Método para manejar datos desde el POSTMAN
                /*
                * jwtFilter: filtro que se encargará de interceptar cada solicitud HTTP para validar el token JWT.
                * UsernamePasswordAuthenticationFilter.class: Es el filtro predeterminado de Spring Security que maneja la autenticación con nombre de usuario y contraseña.
                * */
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)     //Filtro personalizado
                .build();
    }


    /*
    * Con el "DaoAuthenticationProvider" vamos autenticar las credenciales y la contraseña, utilizando un "DaoAuthenticationProvider" que se
    * 1.configura para buscar los datos del usuario(credenciales), este UserDetailsService devuelve una instancia UserDetailsService con más datos
    * 2.Con la contraseña la compara con la que tenemos en la base de datos que se encuentra en el objeto UserDetails
    * 3.Si todo esta correcto devuelve un objeto Authentication valido
     * */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); // Configura el codificador de contraseñas
        provider.setPasswordEncoder(passwordEncoder()); // Usa BCrypt para validar la contraseña
        provider.setUserDetailsService(userDetailsService);   // Establece el servicio para obtener usuarios, Obtiene los usuarios desde la BD
        return provider;
    }

    /*
    * Encargado de recibir una solicitud de AUTENTICACIÓN y DISTRIBUIRLA en diferentes "AuthenticationProvider"
    * disponibles hasta encontrar uno que lo pueda manejar
    * Es el encargado de delegar la autenticación a los AuthenticationProvider registrados.
    * -Este método obtiene un AuthenticationManager preconfigurado desde AuthenticationConfiguration.
    * -AuthenticationConfiguration: Se encarga de autoconfigurar el AuthenticationManager, incluyendo todos los AuthenticationProvider disponibles en el contexto de Spring
    * */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}























