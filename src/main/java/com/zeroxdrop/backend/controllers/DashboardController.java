package com.zeroxdrop.backend.controllers;

import com.zeroxdrop.backend.entities.Usuario;
import com.zeroxdrop.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. Login básico
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        Optional<Usuario> userOpt = usuarioRepository.findByUsername(username);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return ResponseEntity.ok("OK");
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    // 2. Listar los archivos de la carpeta del usuario
    @GetMapping("/archivos")
    public ResponseEntity<List<String>> listarArchivos(@RequestParam String username) {
        String folderPath = System.getProperty("user.dir") + "/uploads/" + username + "/";
        File folder = new File(folderPath);
        List<String> fileNames = new ArrayList<>();

        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        }
        return ResponseEntity.ok(fileNames);
    }

    // 3. Descargar el archivo
    @GetMapping("/descargar")
    public ResponseEntity<Resource> descargarArchivo(@RequestParam String username, @RequestParam String filename) {
        try {
            Path filePath = Paths.get(System.getProperty("user.dir") + "/uploads/" + username + "/" + filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        // Esta cabecera le dice al navegador "descargá esto", no intentes abrirlo en una pestaña
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}