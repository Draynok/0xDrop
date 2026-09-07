package com.zeroxdrop.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Permite que cualquier página web se conecte sin bloqueos de seguridad por ahora
public class FileController {

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("codigo") String codigo) {

        try {
            // Creamos una carpeta "uploads" en el directorio donde esté corriendo el programa
            String folderPath = System.getProperty("user.dir") + "/uploads/";
            File directory = new File(folderPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Guardamos el archivo que nos mandaron
            File destFile = new File(folderPath + file.getOriginalFilename());
            file.transferTo(destFile);

            return ResponseEntity.ok("¡Envío exitoso! Archivo '" + file.getOriginalFilename() + "' enviado al código: " + codigo);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al procesar el envio.");
        }
    }
}