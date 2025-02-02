package com.example.GestionDeUsuarios.service;

import com.example.GestionDeUsuarios.entitys.UserImpl;
import com.example.GestionDeUsuarios.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    /*
    * Aplicamos inyección de dependencias
    * */
//    @Autowired
//    private UserRepository userRepository;

    /*
    * Encriptamos la contraseña
    * Con el parametro "12" indicamos que se hashee 12 veces(mientras más mejor >:D)
    * */
//    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    /*
    * private: Hace que la variable solo sea accesible dentro de la clase(UserService)
    * final: Garantiza que la referencia a JWTService no cambiará después de la inyección.
    * */
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    /*
    * Agregamos el constructor, este constructor inyecta el UserRepository y PasswordEncoder como dependencias(como si fuera inyección dependencias)
    * */
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JWTService jwtService){

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    //Creación de un usuario
    public UserImpl addUser(UserImpl user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    //Obtener todos los usuarios
    public List<UserImpl> getAllUsers(){
        return userRepository.findAll();
    }

    //Método que verifica si un usuario existe o no
    public String verify(UserImpl user) {
        /*
        * Ejemplo:
        *   el "authenticationManager" es el "guardia" del edificio "proyecto" que verificara si nuestras credenciales son correctas
        *   nuestro "UsernamePasswordAuthenticationToken" es como una "tarjeta de acceso" con nuestras credenciales(nombre y contra)
        *   el proceso de revisión del "guardia(authenticationManager") es el método "authenticate()"
        *
        * Si el proceso(authenticate) indica que nuestras credenciales son correctas(si existen en la base de datos) ingresamos al edificio
        * sino son correctas nos bota :(
        * */
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        //Ya teniendo las credenciales vamos a autenticarlas con el método "isAuthenticated"
        if(authentication.isAuthenticated())
            //AQUI TENEMOS QUE DEVOLVER EL TOKEN JWT >:D
            return jwtService.generateToken(user.getUsername());
//            return "Autenticado con exito!";

        return "Autenticación fallida";
    }
}
