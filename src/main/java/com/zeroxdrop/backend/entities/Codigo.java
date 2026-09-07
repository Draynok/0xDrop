package com.zeroxdrop.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "codigos")
@Data
public class Codigo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String valor; // Ej: "DROP-PERMA-123"

    @Column(nullable = false)
    private String tipo; // Acá vamos a guardar "PERMANENTE" o "TEMPORAL"

    // Esta columna solo se va a usar si el tipo es temporal
    private LocalDateTime fechaVencimiento;

    // Relación: Muchos códigos pueden pertenecer a UN usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}