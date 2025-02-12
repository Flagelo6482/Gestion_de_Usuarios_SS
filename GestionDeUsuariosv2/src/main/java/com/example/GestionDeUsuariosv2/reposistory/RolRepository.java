package com.example.GestionDeUsuariosv2.reposistory;

import com.example.GestionDeUsuariosv2.entity.Rol;
import com.example.GestionDeUsuariosv2.entity.RolName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByName(RolName name);
}
