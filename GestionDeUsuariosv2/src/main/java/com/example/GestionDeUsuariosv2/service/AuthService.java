package com.example.GestionDeUsuariosv2.service;

import com.example.GestionDeUsuariosv2.dto.AuthResponse;
import com.example.GestionDeUsuariosv2.entity.UserDetailsImpl;
import com.example.GestionDeUsuariosv2.entity.UserImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JWTService jwtService;

    public String verify(String username, String password) {
        try {
            // Autenticar al usuario
            Authentication authentication = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            logger.info("Autenticación exitosa para el usuario: " + username);
            // Si la autenticación es exitosa, devolver la redirección al dashboard
            return "redirect:/private/dashboard";
        } catch (AuthenticationException ex) {
            logger.error("Error de autenticación para el usuario: " + username, ex);
            // Si la autenticación falla, devolver la redirección al login con un mensaje de error
            return "redirect:/public/login?error";
        }
    }

//    public ResponseEntity<?> verify(UserImpl user) {
//        try {
//            Authentication authentication = manager.authenticate(
//                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
//            );
//
//            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//            String token = jwtService.generateToken(userDetails.getUsername());
//
//            Map<String, String> response = new HashMap<>();
//            response.put("token", token);
//            return ResponseEntity.ok(response);
//        } catch (AuthenticationException ex) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
//        }
//    }

//    public String verify(String username, String password, HttpServletResponse response){
//        try {
//            //Autenticamos al usuario
//            Authentication authentication = manager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, password)
//            );
//
//            //Generamos el token JWT
//            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//            String token = jwtService.generateToken(userDetails.getUsername());
//
//            // Log para verificar el token
//            logger.info("Token generado: " + token);
//
//            //Guardamos el token en una cookie(opcional)
//            Cookie jwtCookie = new Cookie("jwtToken", token);
//            jwtCookie.setHttpOnly(true); //Protegemos la cookie
//            jwtCookie.setPath("/"); //La cookie estara disponible en toda la aplicacion
//            jwtCookie.setSecure(false); // Permitir en HTTP (solo en desarrollo)
//            response.addCookie(jwtCookie);
//
//            logger.info("Cookie agregada: " + jwtCookie.toString()); // Usa el logger
//
//            return "redirect:/private/dashboard";   //Redirigimos al dashboard si el login es exitoso
//        } catch (AuthenticationException ex) {
//            logger.error("Error de autenticación: " + ex.getMessage()); // Usa el logger
//            return "redirect:/public/login?error"; //Redirigimos al login con un mensaje de error
//        }
//    }
}
