package com.example.controller;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/verificador")
public class VerificationController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Panel de verificación - solo accesible para ADMIN y VERIFICADOR
     */
    @GetMapping("/panel")
    public String panel(Model model) {
        // Obtener solo usuarios no verificados para revisar
        List<Usuario> usuariosNoVerificados = usuarioRepository.findAll()
                .stream()
                .filter(u -> !u.getVerificar_titulacion())
                .toList();

        model.addAttribute("usuariosNoVerificados", usuariosNoVerificados);
        return "verificador/panel";
    }

    /**
     * Cambiar el estado de verificación de un usuario
     * Solo ADMIN y VERIFICADOR pueden hacerlo
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
     * Vista de detalles de un usuario para verificación
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

