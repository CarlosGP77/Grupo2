package com.example.controller;

import com.example.model.VerifiedImage;
import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import com.example.service.VerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/verificador")
@CrossOrigin(origins = "*")
public class VerificationController {

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            VerifiedImage image = verificationService.uploadImage(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(image);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            log.error("Error al procesar archivo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al procesar el archivo"));
        } catch (RuntimeException e) {
            log.error("Error en uploadImage", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<VerifiedImage>> getPendingImages() {
        try {
            List<VerifiedImage> images = verificationService.getPendingImages();
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            log.error("Error en getPendingImages", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/verificadas")
    public ResponseEntity<List<VerifiedImage>> getVerifiedImages() {
        try {
            List<VerifiedImage> images = verificationService.getVerifiedImages();
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            log.error("Error en getVerifiedImages", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/verificar/{id}")
    public ResponseEntity<?> verifyImage(
            @PathVariable Long id,
            @RequestParam VerifiedImage.VerificationStatus status,
            @RequestParam(required = false) String comment,
            @RequestParam String verifiedBy) {
        try {
            VerifiedImage image = verificationService.verifyImage(id, status, comment, verifiedBy);
            return ResponseEntity.ok(image);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error en verifyImage", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al verificar imagen"));
        }
    }

    @GetMapping("/ver/{id}")
    public ResponseEntity<?> viewImage(@PathVariable Long id) {
        try {
            byte[] imageContent = verificationService.getImageContent(id);
            if (imageContent == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Imagen no encontrada"));
            }

            VerifiedImage image = verificationService.getImageById(id);
            String mediaType = image.getMimeType() != null ? image.getMimeType() : "application/octet-stream";

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mediaType))
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600")
                .body(imageContent);
        } catch (Exception e) {
            log.error("Error en viewImage", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener imagen"));
        }
    }

    @GetMapping("/descargar/{id}")
    public ResponseEntity<?> downloadImage(@PathVariable Long id) {
        try {
            byte[] imageContent = verificationService.downloadImage(id);
            if (imageContent == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Imagen no encontrada"));
            }

            VerifiedImage image = verificationService.getImageById(id);
            String filename = image.getFilename() != null ? image.getFilename() : ("certificado-" + id);

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getMimeType() != null ? image.getMimeType() : MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(imageContent);
        } catch (Exception e) {
            log.error("Error en downloadImage", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al descargar imagen"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable Long id) {
        try {
            verificationService.deleteImage(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error en deleteImage", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al eliminar imagen"));
        }
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> stats = verificationService.getStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error en getStatistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @GetMapping("/certificados/usuario/{dni}")
    public ResponseEntity<?> getCertificatesByUsuario(@PathVariable String dni) {
        try {
            Usuario usuario = usuarioRepository.findByDni(dni);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
            }

            List<VerifiedImage> certificados = verificationService.getCertificatesByUsuario(usuario);
            return ResponseEntity.ok(certificados);
        } catch (Exception e) {
            log.error("Error en getCertificatesByUsuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener certificados"));
        }
    }

    @GetMapping("/certificados/usuario/{dni}/pendientes")
    public ResponseEntity<?> getPendingCertificatesByUsuario(@PathVariable String dni) {
        try {
            Usuario usuario = usuarioRepository.findByDni(dni);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
            }

            List<VerifiedImage> certificados = verificationService.getPendingCertificatesByUsuario(usuario);
            return ResponseEntity.ok(certificados);
        } catch (Exception e) {
            log.error("Error en getPendingCertificatesByUsuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener certificados pendientes"));
        }
    }

    @GetMapping("/usuarios-con-certificados")
    public ResponseEntity<?> getUsersWithCertificates() {
        try {
            List<VerifiedImage> allCertificates = verificationService.getPendingImages();
            Map<String, Object> result = new HashMap<>();

            Set<String> usuariosSet = new HashSet<>();
            for (VerifiedImage cert : allCertificates) {
                if (cert.getUsuario() != null) {
                    usuariosSet.add(cert.getUsuario().getDni());
                }
            }

            result.put("usuarios", usuariosSet);
            result.put("total", usuariosSet.size());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error en getUsersWithCertificates", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener usuarios"));
        }
    }
}
