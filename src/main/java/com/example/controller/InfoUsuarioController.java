package com.example.controller;

import com.example.model.Reserva;
import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import com.example.service.ReservaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class InfoUsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final ReservaService reservaService;

    public InfoUsuarioController(UsuarioRepository usuarioRepository, ReservaService reservaService) {
        this.usuarioRepository = usuarioRepository;
        this.reservaService = reservaService;
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

        model.addAttribute("usuario", usuario);
        model.addAttribute("reservasUsuario", reservasUsuario);
        model.addAttribute("totalReservasUsuario", reservasUsuario.size());
        model.addAttribute("certificacionesUsuario", certificacionesUsuario);

        return "html/info-usuario";
    }
}


