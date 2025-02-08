package com.example.GestionDeUsuariosv2.service;

import com.example.GestionDeUsuariosv2.entity.Rol;
import com.example.GestionDeUsuariosv2.entity.RolName;
import com.example.GestionDeUsuariosv2.reposistory.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    //Crear un rol
    public Rol crearRol(Rol rol){
        return rolRepository.save(rol);
    }

    //Buscar rol por ID
    public Optional<Rol> obtenerRolPorNombre(String name){
        RolName rolName = RolName.valueOf(name);
        return rolRepository.findByName(rolName);
    }

    //Listar todos los roles
    public List<Rol> listarRoles(){
        return rolRepository.findAll();
    }

    //Eliminar un rol por ID
    public void eliminarRol(Long id){
        rolRepository.deleteById(id);
    }
}
