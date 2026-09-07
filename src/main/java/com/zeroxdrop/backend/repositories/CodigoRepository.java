package com.zeroxdrop.backend.repositories;

import com.zeroxdrop.backend.entities.Codigo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CodigoRepository extends JpaRepository<Codigo, Long> {

    // Esto le enseña a Spring a buscar en la base de datos si un código ingresado existe
    Optional<Codigo> findByValor(String valor);
    // Busca todos los códigos asociados a un usuario
    List<Codigo> findByUsuario(com.zeroxdrop.backend.entities.Usuario usuario);
}