package com.example.GestionDeUsuarios.config;

import com.example.GestionDeUsuarios.entitys.UserImpl;
import com.example.GestionDeUsuarios.service.JWTService;
import com.example.GestionDeUsuarios.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
* Encargado de VALIDAR un token JWT
* */
@Service
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JWTService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Obetenemos el string de "request" llamado "Authorization"
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        /*
        * Si el encabezado no es nulo y comienza con la palabra "Bearer " seguimos el flujo
        * */
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            //Cortamos 7 posiciones que serían "Bearer " para obtener solamente el token
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
        }

        /*
        * Si el nombre de usuario no es null y es la autenticación es nula(ósea si es la primera vez autenticandoce)
        * */
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            //Con esto obtendremos los datos de la base de datos
            UserDetails userDetails = context.getBean(UserDetailsServiceImpl.class).loadUserByUsername(username);

            if(jwtService.validateToken(token, userDetails)){

                /*
                * "UsernamePasswordAuthenticationToken" es token de autenticación que se usa para representar a un usuario autenticado
                * userDetails: Contiene la información del usuario autenticado.
                * null: Representa la contraseña (que en este caso no es necesaria porque el usuario ya está autenticado).
                * userDetails.getAuthorities(): Obtiene los roles o permisos del usuario.
                * */
                // 🔥 Aquí se crea el token de autenticación para Spring Security
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 🌍 Asociar detalles de la solicitud HTTP al token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 🛡️ Registrar al usuario en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        //Continuamos con los demas filtros
        filterChain.doFilter(request, response);
    }
}
