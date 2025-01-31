package com.example.GestionDeUsuarios.controller;

import com.example.GestionDeUsuarios.entitys.UserImpl;
import com.example.GestionDeUsuarios.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public UserImpl createUser(@RequestBody UserImpl user){
        return userService.addUser(user);
    }

    @GetMapping("/allUsers")
    public List<UserImpl> findUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }


    /*
    * Método para iniciar sesión con las credenciales que tenemos en base de datos
    * @RequestBody: Verifica el JSON que le pasamos con los datos(no las credenciales)
    * */
    @PostMapping("/login")
    public String login(@RequestBody UserImpl user){
        //Agregaremos un metodo para verificar si el usuario que esta iniciando sesión existe o no
        return userService.verify(user);
    }
}
