package com.example.config;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@SuppressWarnings("unused")
public class NavbarViewAdvice {

    @ModelAttribute("usuarioAutenticado")
    public boolean usuarioAutenticado() {
        return authenticationNoAnon() != null;
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
        return authentication != null ? authentication.getName() : "";
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
