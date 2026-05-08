package com.example.controller;

import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador de administración - solo accesible para usuarios con rol ADMIN.
 * Rutas protegidas en SecurityConfig: /admin/** → requiere ADMIN
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    /**
     * Panel de control administrativo
     * Solo accesible para ADMIN
     */
    @GetMapping("/panel")
    public String adminPanel(Model model) {
        // Estadísticas del sistema
        long totalUsuarios = usuarioRepository.count();
        long usuariosNoVerificados = usuarioRepository.findAll()
                .stream()
                .filter(u -> !u.getVerificar_titulacion())
                .count();
        long totalCursos = cursoRepository.count();
        long totalReservas = reservaRepository.count();

        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("usuariosNoVerificados", usuariosNoVerificados);
        model.addAttribute("totalCursos", totalCursos);
        model.addAttribute("totalReservas", totalReservas);

        return "admin/panel";
    }

    /**
     * Estadísticas en JSON (para consumo desde frontend)
     * GET /admin/api/stats
     */
    @GetMapping("/api/stats")
    @ResponseBody
    public String getStats() {
        long totalUsuarios = usuarioRepository.count();
        long usuariosNoVerificados = usuarioRepository.findAll()
                .stream()
                .filter(u -> !u.getVerificar_titulacion())
                .count();
        long totalCursos = cursoRepository.count();
        long totalReservas = reservaRepository.count();

        return String.format(
            "{\"totalUsuarios\": %d, \"usuariosNoVerificados\": %d, \"totalCursos\": %d, \"totalReservas\": %d}",
            totalUsuarios, usuariosNoVerificados, totalCursos, totalReservas
        );
    }
}
