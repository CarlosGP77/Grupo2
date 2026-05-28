package com.example.config;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;

@ControllerAdvice
@SuppressWarnings("unused")
public class NavbarViewAdvice {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @ModelAttribute("usuarioAutenticado")
    public boolean usuarioAutenticado() {
        return currentUsuario() != null;
    }

    @ModelAttribute("usuarioEsAdmin")
    public boolean usuarioEsAdmin() {
        return hasRole("ADMIN");
    }

    @ModelAttribute("usuarioEsVerificador")
    public boolean usuarioEsVerificador() {
        return hasRole("VERIFICADOR");
    }

    @ModelAttribute("nombreUsuario")
    public String nombreUsuario() {
        Authentication authentication = authenticationNoAnon();
        Usuario usuario = currentUsuario();
        if (usuario != null && usuario.getNombre_completo() != null && !usuario.getNombre_completo().isBlank()) {
            return usuario.getNombre_completo();
        }
        return authentication != null ? authentication.getName() : "";
    }

    @ModelAttribute("usuario")
    public Usuario usuario() {
        return currentUsuario();
    }

    private Usuario currentUsuario() {
        Authentication authentication = authenticationNoAnon();
        if (authentication == null) {
            return null;
        }

        try {
            Usuario usuario = usuarioRepository.findByEmail(authentication.getName());
            return usuario;
        } catch (Exception e) {
            return null;
        }
    }

    private Authentication authenticationNoAnon() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return authentication;
    }

    private boolean hasRole(String role) {
        Authentication authentication = authenticationNoAnon();
        if (authentication == null) {
            return false;
        }
        String expectedAuthority = "ROLE_" + role;
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> expectedAuthority.equals(authority.getAuthority()));
    }
}
