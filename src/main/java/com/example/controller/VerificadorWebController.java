package com.example.controller;

import com.example.model.Usuario;
import com.example.model.VerifiedImage;
import com.example.repository.UsuarioRepository;
import com.example.repository.VerifiedImageRepository;
import com.example.service.VerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/verificador")
@PreAuthorize("hasAnyRole('VERIFICADOR', 'ADMIN')")
public class VerificadorWebController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VerificationService verificationService;

    @GetMapping("/panel")
    public String panel() {
        // Mantiene compatibilidad con enlaces existentes del navbar y panel admin.
        return "redirect:/verificador/certificados";
    }

    @GetMapping("/certificados")
    public String certificados(Model model) {
        List<VerifiedImage> certificadosPendientes = verificationService.getPendingImages();

        Map<Usuario, List<VerifiedImage>> certificadosPorUsuario = new HashMap<>();
        for (VerifiedImage cert : certificadosPendientes) {
            if (cert.getUsuario() != null) {
                certificadosPorUsuario.computeIfAbsent(cert.getUsuario(), k -> new ArrayList<>()).add(cert);
            }
        }

        model.addAttribute("certificadosPorUsuario", certificadosPorUsuario);
        model.addAttribute("totalUsuarios", certificadosPorUsuario.size());
        model.addAttribute("totalCertificados", certificadosPendientes.size());

        return "verificador/certificados";
    }

    @GetMapping("/certificados/usuario/{dni}")
    public String certificadosUsuario(@PathVariable String dni, Model model) {
        Usuario usuario = usuarioRepository.findByDni(dni);
        if (usuario == null) {
            model.addAttribute("status", 404);
            model.addAttribute("message", "Usuario no encontrado");
            model.addAttribute("error", "Usuario no encontrado");
            return "html/error";
        }

        List<VerifiedImage> certificados = verificationService.getCertificatesByUsuario(usuario);
        List<VerifiedImage> certificadosPendientes = verificationService.getPendingCertificatesByUsuario(usuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("certificados", certificados);
        model.addAttribute("certificadosPendientes", certificadosPendientes);
        model.addAttribute("totalCertificados", certificados.size());
        model.addAttribute("totalPendientes", certificadosPendientes.size());

        return "verificador/certificados-usuario";
    }

    @PostMapping("/certificados/{id}/verificar")
    @ResponseBody
    public ResponseEntity<?> verificarCertificado(
            @PathVariable Long id,
            @RequestParam VerifiedImage.VerificationStatus status,
            @RequestParam(required = false) String comment,
            @RequestParam String verifiedBy) {
        try {
            verificationService.verifyImage(id, status, comment, verifiedBy);
            log.info("Certificado {} verificado como {}", id, status);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Certificado verificado correctamente"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("success", false, "error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al verificar certificado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "error", "Error al verificar el certificado"));
        }
    }

    @PostMapping("/certificados/{id}/rechazar")
    @ResponseBody
    public ResponseEntity<?> rechazarCertificado(
            @PathVariable Long id,
            @RequestParam(required = false) String razon,
            @RequestParam String verifiedBy) {
        try {
            verificationService.verifyImage(
                id,
                VerifiedImage.VerificationStatus.REJECTED,
                razon,
                verifiedBy
            );
            log.info("Certificado {} rechazado", id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Certificado rechazado correctamente"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("success", false, "error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al rechazar certificado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "error", "Error al rechazar el certificado"));
        }
    }
}