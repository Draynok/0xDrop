package com.zeroxdrop.backend.controllers;

import com.zeroxdrop.backend.entities.Codigo;
import com.zeroxdrop.backend.entities.Usuario;
import com.zeroxdrop.backend.repositories.CodigoRepository;
import com.zeroxdrop.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FileController {

    @Autowired
    private CodigoRepository codigoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "codigo", required = false) String codigoStr,
            @RequestParam("destinatario") String destinatario) {

        if (files.size() > 5) {
            return ResponseEntity.badRequest().body("Error: Máximo 5 archivos por drop.");
        }

        Usuario dueño = null;

        // ESCENARIO 1: El emisor ingresó un código
        if (codigoStr != null && !codigoStr.trim().isEmpty()) {
            Optional<Codigo> codigoOpt = codigoRepository.findByValor(codigoStr);
            if (codigoOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: Código inválido.");
            }

            Codigo codigo = codigoOpt.get();
            dueño = codigo.getUsuario();

            if (!dueño.getUsername().equals(destinatario)) {
                return ResponseEntity.badRequest().body("Error: El código no coincide con el destinatario.");
            }

            if ("TEMPORAL".equals(codigo.getTipo()) && codigo.getFechaVencimiento() != null) {
                if (LocalDateTime.now().isAfter(codigo.getFechaVencimiento())) {
                    return ResponseEntity.badRequest().body("Error: Código expirado.");
                }
            }
        }
        // ESCENARIO 2: No puso código (Recibimiento Libre)
        else {
            Optional<Usuario> userOpt = usuarioRepository.findByUsername(destinatario);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: Destinatario no encontrado.");
            }

            dueño = userOpt.get();
            if (!dueño.isAceptaRecibimientoLibre()) {
                return ResponseEntity.badRequest().body("Error: El usuario tiene el recibimiento libre bloqueado. Se requiere un código.");
            }
        }

        try {
            // Guardar archivos
            String folderPath = System.getProperty("user.dir") + "/uploads/" + dueño.getUsername() + "/";
            File directory = new File(folderPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            for (MultipartFile file : files) {
                File destFile = new File(folderPath + file.getOriginalFilename());
                file.transferTo(destFile);
            }

            return ResponseEntity.ok("Datos asegurados en la bandeja de " + dueño.getUsername());

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error interno al procesar el drop.");
        }
    }
}