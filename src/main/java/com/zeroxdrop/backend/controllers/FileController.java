package com.zeroxdrop.backend.controllers;

import com.zeroxdrop.backend.entities.Codigo;
import com.zeroxdrop.backend.entities.Usuario;
import com.zeroxdrop.backend.repositories.CodigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FileController {

    // Traemos el repositorio para buscar en la tabla de códigos
    @Autowired
    private CodigoRepository codigoRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("codigo") String codigoStr) {

        // 1. Buscar si el código existe en la base de datos
        Optional<Codigo> codigoOpt = codigoRepository.findByValor(codigoStr);
        if (codigoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: Drop fallido. Código inválido o inexistente.");
        }

        Codigo codigo = codigoOpt.get();

        // 2. Si el código es temporal, verificar que no esté vencido
        if ("TEMPORAL".equals(codigo.getTipo()) && codigo.getFechaVencimiento() != null) {
            if (LocalDateTime.now().isAfter(codigo.getFechaVencimiento())) {
                return ResponseEntity.badRequest().body("Error: Este código temporal ya expiró.");
            }
        }

        // 3. Identificar al dueño del código
        Usuario dueño = codigo.getUsuario();

        try {
            // 4. Crear una carpeta ESPECÍFICA para este usuario
            String folderPath = System.getProperty("user.dir") + "/uploads/" + dueño.getUsername() + "/";
            File directory = new File(folderPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Guardar el archivo ahí adentro
            File destFile = new File(folderPath + file.getOriginalFilename());
            file.transferTo(destFile);

            return ResponseEntity.ok("¡0xDrop exitoso! Archivo asegurado en la bandeja de: " + dueño.getUsername());

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error interno al procesar el drop.");
        }
    }
}