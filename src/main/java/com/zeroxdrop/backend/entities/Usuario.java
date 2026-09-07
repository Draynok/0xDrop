package com.zeroxdrop.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data // Anotación de Lombok que genera Getters, Setters, toString, etc.
@NoArgsConstructor // Lombok genera un constructor vacío, necesario para JPA
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // Configuración para el "Recibimiento libre de archivos"
    @Column(nullable = false)
    private boolean aceptaRecibimientoLibre = false;
}