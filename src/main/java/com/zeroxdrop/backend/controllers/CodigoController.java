package com.zeroxdrop.backend.controllers;

import com.zeroxdrop.backend.entities.Codigo;
import com.zeroxdrop.backend.entities.Usuario;
import com.zeroxdrop.backend.repositories.CodigoRepository;
import com.zeroxdrop.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/codigos")
@CrossOrigin(origins = "*")
public class CodigoController {

    @Autowired
    private CodigoRepository codigoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar todos los códigos de un usuario
    @GetMapping("/listar")
    public ResponseEntity<List<Codigo>> listarCodigos(@RequestParam String username) {
        Optional<Usuario> userOpt = usuarioRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(codigoRepository.findByUsuario(userOpt.get()));
    }

    // Generar un código nuevo
    @PostMapping("/generar")
    public ResponseEntity<String> generarCodigo(@RequestParam String username, @RequestParam String tipo) {
        Optional<Usuario> userOpt = usuarioRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        Codigo nuevoCodigo = new Codigo();
        // Generamos un string aleatorio de 6 caracteres
        String randomStr = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String valor = "DROP-" + randomStr;

        nuevoCodigo.setValor(valor);
        nuevoCodigo.setTipo(tipo.toUpperCase());
        nuevoCodigo.setUsuario(userOpt.get());

        // Si es temporal, le seteamos vencimiento a 2 horas desde ahora
        if (tipo.equalsIgnoreCase("TEMPORAL")) {
            nuevoCodigo.setFechaVencimiento(LocalDateTime.now().plusHours(2));
        }

        codigoRepository.save(nuevoCodigo);
        return ResponseEntity.ok(valor);
    }
}