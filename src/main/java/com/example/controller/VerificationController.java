package com.example.controller;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para verificación de credenciales de usuarios.
 * Solo accesible a usuarios con rol ADMIN o VERIFICADOR.
 *
 * Rutas protegidas en SecurityConfig:
 * - /verificador/** → requiere ADMIN o VERIFICADOR
 */
@Controller
@RequestMapping("/verificador")
public class VerificationController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Panel de verificación - muestra usuarios sin verificar
     * Solo ADMIN y VERIFICADOR tienen acceso (protegido en SecurityConfig)
     */
    @GetMapping("/panel")
    public String panel(Model model) {
        List<Usuario> usuariosNoVerificados = usuarioRepository.findAll()
                .stream()
                .filter(u -> !u.getVerificar_titulacion())
                .toList();

        model.addAttribute("usuariosNoVerificados", usuariosNoVerificados);
        model.addAttribute("totalNoVerificados", usuariosNoVerificados.size());
        return "verificador/panel";
    }

    /**
     * API REST: Listar usuarios sin verificar (JSON)
     * GET /verificador/api/usuarios-sin-verificar
     */
    @GetMapping("/api/usuarios-sin-verificar")
    @ResponseBody
    public List<Usuario> usuariosSinVerificar() {
        return usuarioRepository.findAll()
                .stream()
                .filter(u -> !u.getVerificar_titulacion())
                .toList();
    }

    /**
     * Form: Cambiar el estado de verificación de un usuario
     * POST /verificador/cambiar-verificacion?dni=xxx&verificado=true/false
     */
    @PostMapping("/cambiar-verificacion")
    public String cambiarVerificacion(@RequestParam String dni,
                                       @RequestParam boolean verificado) {
        Usuario u = usuarioRepository.findByDni(dni);
        if (u != null) {
            u.setVerificar_titulacion(verificado);
            usuarioRepository.save(u);
        }
        return "redirect:/verificador/panel";
    }

    /**
     * API REST: Cambiar verificación (JSON)
     * POST /verificador/api/cambiar-verificacion?dni=xxx&verificado=true/false
     */
    @PostMapping("/api/cambiar-verificacion")
    @ResponseBody
    public String cambiarVerificacionApi(@RequestParam String dni,
                                          @RequestParam boolean verificado) {
        Usuario u = usuarioRepository.findByDni(dni);
        if (u != null) {
            u.setVerificar_titulacion(verificado);
            usuarioRepository.save(u);
            return "{\"status\": \"success\", \"message\": \"Usuario " + (verificado ? "verificado" : "sin verificar") + ": " + dni + "\"}";
        }
        return "{\"status\": \"error\", \"message\": \"Usuario no encontrado\"}";
    }

    /**
     * Ver detalles de un usuario específico
     * GET /verificador/usuario?dni=xxx
     */
    @GetMapping("/usuario")
    public String verUsuario(@RequestParam String dni, Model model) {
        Usuario u = usuarioRepository.findByDni(dni);
        if (u != null) {
            model.addAttribute("usuario", u);
            return "verificador/usuario-detail";
        }
        return "redirect:/verificador/panel";
    }
}
