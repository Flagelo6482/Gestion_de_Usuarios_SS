package com.example.GestionDeUsuariosv2.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {



    //Clave secreta para firmar y validar tokens JWT
    private String secretKey = "";

    public JWTService(){
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }


    public String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();
        // Agrega datos adicionales si lo necesitas, por ejemplo:
        // claims.put("role", "ROLE_USER");

        return Jwts.builder()
                .claims()   //Contiene los datos del usuario como roles, email, etc
                .add(claims)
                .subject(username)      //Guardamos el nombre de usuario en el token
                .issuedAt(new Date(System.currentTimeMillis()))     //Fecha de emision
                .expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000))      //Fecha de expiración(30 minutos)
                .and()
                .signWith(getKey())
                .compact();
    }


    private SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
