package com.example.GestionDeUsuariosv2.init;

import com.example.GestionDeUsuariosv2.entity.Rol;
import com.example.GestionDeUsuariosv2.entity.RolName;
import com.example.GestionDeUsuariosv2.reposistory.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private RolRepository repository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //Verificar si existe el rol USER en caso que no, lo crea
        if (!repository.findByName(RolName.ROLE_USER).isPresent()) {
            Rol userRole = new Rol();
            userRole.setName(RolName.ROLE_USER);
            repository.save(userRole);
        }

        //Verificar si existe el rol ADMIN en caso que no, lo crea
        if (!repository.findByName(RolName.ROLE_ADMIN).isPresent()) {
            Rol adminRole = new Rol();
            adminRole.setName(RolName.ROLE_ADMIN);
            repository.save(adminRole);
        }

        System.out.println("Roles ADMIN y USER verificados/creados!!!");
    }
}
