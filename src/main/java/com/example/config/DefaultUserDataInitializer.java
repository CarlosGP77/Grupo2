package com.example.config;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class exDefaultUserDataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserDataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        ensureUser("1111111", "Administrador del Sistema", "admin@mourosub.com", "Admin_123", Usuario.Rol.ADMIN);
        ensureUser("verificador", "Verificador de Credenciales", "verificador@mourosub.com", "Admin_123", Usuario.Rol.VERIFICADOR);
        ensureUser("prueba", "Usuario Prueba", "prueba@prueba.com", "1234", Usuario.Rol.USUARIO);
    }

    private void ensureUser(String dni, String nombreCompleto, String email, String password, Usuario.Rol rol) {
        try {
            Usuario usuario = usuarioRepository.findByEmail(email);
            if (usuario == null) {
                usuario = usuarioRepository.findByDni(dni);
            }
            if (usuario == null) {
                usuario = new Usuario();
                usuario.setDni(dni);
            }

            usuario.setNombre_completo(nombreCompleto);
            usuario.setEmail(email);
            usuario.setPassword(passwordEncoder.encode(password));
            usuario.setRol(rol);
            usuario.setVerificar_titulacion(rol == Usuario.Rol.ADMIN || rol == Usuario.Rol.VERIFICADOR);

            usuarioRepository.save(usuario);
            log.info("Usuario base asegurado: {} ({})", email, rol);
        } catch (Exception e) {
            log.warn("No se pudo asegurar el usuario base {}: {}", email, e.getMessage());
        }
    }
}

