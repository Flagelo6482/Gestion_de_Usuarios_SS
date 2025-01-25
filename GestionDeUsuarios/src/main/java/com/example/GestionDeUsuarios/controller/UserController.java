package com.example.GestionDeUsuarios.controller;

import com.example.GestionDeUsuarios.entitys.UserImpl;
import com.example.GestionDeUsuarios.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
