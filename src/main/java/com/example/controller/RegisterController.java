package com.example.controller;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
public class RegisterController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String formRegistro(Model model) {
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", new Usuario());
        }
        return "html/register";
    }

    @PostMapping("/register")
    public String registrar(
            @ModelAttribute("usuario") Usuario usuario,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model
    ) {
        usuario.setDni(normalizar(usuario.getDni()));
        usuario.setNombre_completo(normalizar(usuario.getNombre_completo()));
        usuario.setEmail(normalizarEmail(usuario.getEmail()));
        usuario.setLicencia(normalizar(usuario.getLicencia()));
        usuario.setTitulaciones(normalizarTexto(usuario.getTitulaciones()));
        usuario.setPoliza_seguro(normalizar(usuario.getPoliza_seguro()));
        usuario.setTelefono(normalizar(usuario.getTelefono()));
        usuario.setTelefono_contacto(normalizar(usuario.getTelefono_contacto()));

        if (usuario.getDni() == null || usuario.getDni().isBlank()
                || usuario.getNombre_completo() == null || usuario.getNombre_completo().isBlank()
                || usuario.getEmail() == null || usuario.getEmail().isBlank()
                || password == null || password.isBlank()) {
            model.addAttribute("error", "Rellena los campos obligatorios.");
            return "html/register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "html/register";
        }

        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            model.addAttribute("error", "Ese correo ya está registrado.");
            return "html/register";
        }

        if (usuarioRepository.findByDni(usuario.getDni()) != null) {
            model.addAttribute("error", "Ese DNI ya está registrado.");
            return "html/register";
        }

        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRol(Usuario.Rol.USUARIO);
        usuario.setVerificar_titulacion(false);
        usuarioRepository.save(usuario);

        return "redirect:/login?registered";
    }

    private String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }

    private String normalizarEmail(String valor) {
        return valor == null ? null : valor.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizarTexto(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isBlank() ? null : limpio;
    }
}

