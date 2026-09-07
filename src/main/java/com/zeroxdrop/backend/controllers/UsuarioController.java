package com.zeroxdrop.backend.controllers;

import com.zeroxdrop.backend.entities.Usuario;
import com.zeroxdrop.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario nuevoUsuario) {
        // Primero nos fijamos si ese nombre de usuario ya está ocupado
        if (usuarioRepository.findByUsername(nuevoUsuario.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: El usuario ya existe.");
        }

        // Si está libre, lo guardamos en la base de datos
        usuarioRepository.save(nuevoUsuario);
        return ResponseEntity.ok("¡Usuario '" + nuevoUsuario.getUsername() + "' registrado en 0xDrop con éxito!");
    }
}