package com.example.GestionDeUsuariosv2.service;

import com.example.GestionDeUsuariosv2.dto.AuthResponse;
import com.example.GestionDeUsuariosv2.entity.UserDetailsImpl;
import com.example.GestionDeUsuariosv2.entity.UserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JWTService jwtService;

    public ResponseEntity<?> verify(UserImpl user) {
        try {
            //Autenticamos las credenciales
            Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            //Obtener el usuario autenticado desde UserDetailsImpl
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            UserImpl usuarioAutenticado = userDetails.getUser();

            //Generar el token con el username del usuario autenticado
            String token = jwtService.generateToken(usuarioAutenticado.getUsername());

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException ex){
            //Si falla la autenticación retornamos un error 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales invalidas oe");
        }
    }
}
