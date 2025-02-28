package com.example.GestionDeUsuariosv2.config;

import com.example.GestionDeUsuariosv2.service.JWTService;
import com.example.GestionDeUsuariosv2.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
* Encargado de FILTRAR un token JWT, donde interceptaremos las solicitudes entrantes para extraer
* el token del HEADER(Authorization) con el prefijo "Bearer", lo validamos si es correcto
* establecera la AUTENTICACIÓN en el contexto de Spring Security
*
* Extendremos de "OncePerRequestFilter" para asegurarnos de que se ejecute una sola vez por solicitud :D
* */
@Service
public class JwtFilter extends OncePerRequestFilter {

    //Inyectamos al servicio donde tenemos los metodos para validar los datos de un token JWT
    @Autowired
    private JWTService service;

    /*
    * Inyectamos nuestra clase personalizada que envuelve nuestra entidad para que sea un objeto "UserDeatilsService"
    * para obtener los datos desde la base de datos
    * */
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Ignorar la verificación del token en la ruta de login
        if (request.getServletPath().equals("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Extraemos el header Authorization
        final String autHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        //Verificamos si el header se encuentra y contiene el prefijo "Bearer "
        if (autHeader != null && autHeader.startsWith("Bearer ")) {
            token = autHeader.substring(7); //Cortamos 7 espacios que son "Bearer " para tener solamente el token JWT
            try {
                username = service.extractUserName(token);  //Extraemos el usuario del token
            } catch (Exception e){
                logger.error("Error al extraer el username del token :"+e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalido o mal formado");
                return; // Detiene la ejecución del filtro para evitar continuar con la solicitud
            }
        }

        //Si se obtuvo el username y el contexto aun no tiene autenticación(si es la primera vez que se autentica el usuario)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            if (service.isTokenExpired(token)) {
                logger.error("Token expirado para el usuario: "+username);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expirado. Por favor inicie sesión nuevamente.");
                return; // Detenemos la ejecución
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);  //Buscamos al usuario en la base de datos

            if (service.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //Continuamos con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
