package com.example.GestionDeUsuariosv2.reposistory;

import com.example.GestionDeUsuariosv2.entity.UserImpl;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserImpl, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<UserImpl> findByEmail(String email);   // ✅ Spring Data JPA genera la consulta
    Optional<UserImpl> findByUsername(String username);
}
