package com.example.controller;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/verificador")
public class VerificationController {

    @Autowired
    private UsuarioRepository usuarioRepository;
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

    @GetMapping("/api/usuarios-sin-verificar")
    @ResponseBody
    public List<Usuario> usuariosSinVerificar() {
        return usuarioRepository.findAll()
                .stream()
                .filter(u -> !u.getVerificar_titulacion())
                .toList();
    }

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
