package com.example.GestionDeUsuariosv2.config;

import com.example.GestionDeUsuariosv2.service.JWTService;
import com.example.GestionDeUsuariosv2.service.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/*
* Encargado de FILTRAR un token JWT, donde interceptaremos las solicitudes entrantes para extraer
* el token del HEADER(Authorization) con el prefijo "Bearer", lo validamos si es correcto
* establecera la AUTENTICACIÓN en el contexto de Spring Security
*
* Extendremos de "OncePerRequestFilter" para asegurarnos de que se ejecute una sola vez por solicitud :D
* */
@Service
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    //Inyectamos al servicio donde tenemos los metodos para validar los datos de un token JWT
    @Autowired
    private JWTService service;

    /*
    * Inyectamos nuestra clase personalizada que envuelve nuestra entidad para que sea un objeto "UserDeatilsService"
    * para obtener los datos desde la base de datos
    * */
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Ignorar la verificación del token en rutas públicas
        List<String> publicPaths = Arrays.asList("/", "/login", "/register", "/public/**");

        // Verificamos si la ruta actual es pública
        String path = request.getServletPath();
        if (publicPaths.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response); // Ignoramos la verificación del token
            return;
        }

        // Extraer el token de la cookie
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        logger.info("Token extraído de la cookie: " + token); // Usa el logger

        // Si no hay token, redirigir al login
        if (token == null) {
            logger.warn("No se encontró el token en la cookie"); // Usa el logger
            response.sendRedirect("/public/login");
            return;
        }

        // Validar el token
        String username = null;
        try {
            username = service.extractUserName(token); // Extraer el nombre de usuario del token
            logger.info("Usuario extraído del token: " + username);
        } catch (ExpiredJwtException e) {
            logger.error("Token expirado para el usuario: " + e.getClaims().getSubject());
            response.sendRedirect("/public/login"); // Redirigir al login si el token expiró
            return;
        } catch (Exception e) {
            logger.error("Error al extraer el username del token: " + e.getMessage());
            response.sendRedirect("/public/login"); // Redirigir al login si hay un error
            return;
        }

        // Si se obtuvo el username y el contexto aún no tiene autenticación
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (service.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("Autenticación establecida para el usuario: " + username);
            } else {
                logger.warn("Token no válido para el usuario: " + username);
                response.sendRedirect("/public/login"); // Redirigir al login si el token no es válido
                return;
            }
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
