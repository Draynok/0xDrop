package com.zeroxdrop.backend.repositories;

import com.zeroxdrop.backend.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Con solo nombrar el método así, Spring Boot automáticamente sabe cómo buscar un usuario por su nombre
    Optional<Usuario> findByUsername(String username);
}