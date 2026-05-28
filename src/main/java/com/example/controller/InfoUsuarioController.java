package com.example.controller;

import com.example.model.Reserva;
import com.example.model.Usuario;
import com.example.model.VerifiedImage;
import com.example.repository.UsuarioRepository;
import com.example.service.ReservaService;
import com.example.service.VerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class InfoUsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final ReservaService reservaService;
    private final VerificationService verificationService;

    public InfoUsuarioController(UsuarioRepository usuarioRepository, ReservaService reservaService, VerificationService verificationService) {
        this.usuarioRepository = usuarioRepository;
        this.reservaService = reservaService;
        this.verificationService = verificationService;
    }

    @GetMapping("/info-usuario")
    public String infoUsuario(Authentication authentication, Model model) {
        Usuario usuario = authentication != null ? usuarioRepository.findByEmail(authentication.getName()) : null;
        List<Reserva> reservasUsuario = usuario != null ? reservaService.obtenerPorUsuario(usuario) : List.of();
        List<String> certificacionesUsuario = usuario != null && usuario.getTitulaciones() != null
                ? Arrays.stream(usuario.getTitulaciones().split("[\\n,;]+"))
                .map(String::trim)
                .filter(valor -> !valor.isBlank())
                .collect(Collectors.toList())
                : List.of();

        List<VerifiedImage> certificadosSubidos = usuario != null
                ? verificationService.getCertificatesByUsuario(usuario)
                : List.of();

        model.addAttribute("usuario", usuario);
        model.addAttribute("reservasUsuario", reservasUsuario);
        model.addAttribute("totalReservasUsuario", reservasUsuario.size());
        model.addAttribute("certificacionesUsuario", certificacionesUsuario);
        model.addAttribute("certificadosSubidos", certificadosSubidos);

        return "html/info-usuario";
    }

    @PostMapping("/api/info-usuario/subir-certificado")
    public ResponseEntity<?> subirCertificado(@RequestParam("file") MultipartFile file, Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario no autenticado"));
            }

            Usuario usuario = usuarioRepository.findByEmail(authentication.getName());
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario no encontrado"));
            }

            VerifiedImage certificado = verificationService.uploadCertificate(file, usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(certificado);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación en subida de certificado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            log.error("Error IO al procesar certificado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al procesar el archivo"));
        } catch (RuntimeException e) {
            log.error("Error en subirCertificado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }
}


