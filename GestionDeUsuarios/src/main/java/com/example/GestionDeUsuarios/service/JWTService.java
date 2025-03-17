package com.example.GestionDeUsuarios.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/*
* Clase que generara el token personalizado por nosotros
* */
@Service
public class JWTService {

    //es la clave usada para firmar y validar tokens JWT.
    private String secretKey = "";

    /*
    * Este fragmento de código genera una clave secreta (secretKey) de manera dinámica al inicializar el servicio
    * JWTService. Esta clave se utilizará más adelante para firmar los tokens JWT.
    * KeyGenerator.getInstance("HmacSHA256"): Crea un generador de claves criptográficas que usa el algoritmo HmacSHA256 (Hash-based Message Authentication Code con SHA-256).
    * SecretKey sk = keyGenerator.generateKey();: genera una clave secreta (SecretKey) aleatoria utilizando HmacSHA256.
    *
    * sk.getEncoded(): Obtiene los bytes de la clave generada
    * Base64.getEncoder().encodeToString(...): Convierte la clave en un formato Base64 para almacenarla como una cadena.
    * ¿Por qué se convierte a Base64? Porque el token JWT necesita una clave en formato legible como String en lugar de bytes.
    * */
    public JWTService(){
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String username) {
        /*
         * Map<>: Diccionario
         * String: Indica que las claves serán tipo String
         * Object: Indica que los valores de cualquier tipo boolean, string, integer, etc
         * */
        Map<String, Object> claims = new HashMap<>();


        return Jwts.builder()
                .claims()//claims: Contiene los datos del usuario role, email, etc
                .add(claims)
                .subject(username)      //Guarda el nombre de usuario dentro del token
                .issuedAt(new Date(System.currentTimeMillis()))                     //Fecha de emisión del token.
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))    //Fecha en la que el token expira
                .and()
                .signWith(getKey())         //Firma el token para que sea seguro.
                .compact();                 //finaliza el proceso y devuelve el token en formato String.
    }

    /*
    * getKey: convierte secretKey en una clave válida para firmar JWTs.
    * Decoders.BASE64.decode(secretKey): para convertir la clave en bytes.
    * Ket: Generamos una clave con este tipo de objeto "Key"
    * */
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        //Extraemos el nombre de usuario, del token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
}
