package com.example.GestionDeUsuariosv2.config;

import com.example.GestionDeUsuariosv2.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


/*
* Archivo que gestiona la seguridad y condiciones de las rutas
* */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //Llamamos a nuestra entidad que implementa "UserDetailsService" para cargar los usuario como parte del contexto de Spring
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    //Llamamos al filtro del token JWT
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Desactivar CSRF para simplificar
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll() // Rutas públicas
                        .requestMatchers("/private/**").authenticated() // Rutas privadas
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/public/login") // Página de login personalizada
                        .defaultSuccessUrl("/private/dashboard") // Redirigir al dashboard después del login
                        .failureUrl("/public/login?error") // Redirigir al login con un mensaje de error si falla
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout") // Ruta para cerrar sesión
                        .logoutSuccessUrl("/public/login") // Redirigir al login después de cerrar sesión
                        .invalidateHttpSession(true) // Invalidar la sesión
                        .deleteCookies("JSESSIONID") // Eliminar la cookie de sesión
                        .permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // Usar sesiones
                .build();
    }


    /*
    * Creamos un PROVIDER para que llame a los responsables que se encargar de traer los datos del usuario
    * de la base de datos y desencriptar la contraseña del usuario
    * */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService); //Establecemos el servicio que cargar los datos
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    //Generamos un bean para encriptar nuestra contraseña
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*
     * Encargado de recibir una solicitud de AUTENTICACIÓN y DISTRIBUIRLA en diferentes "AuthenticationProvider"
     * disponibles hasta encontrar uno que lo pueda manejar
     * Es el encargado de delegar la autenticación a los AuthenticationProvider registrados.
     * -Este método obtiene un AuthenticationManager preconfigurado desde AuthenticationConfiguration.
     * -AuthenticationConfiguration: Se encarga de autoconfigurar el AuthenticationManager, incluyendo todos los
     * AuthenticationProvider disponibles en el contexto de Spring
     * */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
