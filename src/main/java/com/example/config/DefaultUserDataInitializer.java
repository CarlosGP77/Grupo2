package com.example.config;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DefaultUserDataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserDataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        upsertDefaultUser(
                "1111111",
                "Administrador del Sistema",
                "admin@mourosub.com",
                "Admin_123",
                Usuario.Rol.ADMIN
        );

        upsertDefaultUser(
                "verificador",
                "Verificador de Credenciales",
                "verificador@mourosub.com",
                "Admin_123",
                Usuario.Rol.VERIFICADOR
        );

        upsertDefaultUser(
                "prueba",
                "Usuario Prueba",
                "prueba@prueba.com",
                "1234",
                Usuario.Rol.USUARIO
        );
    }

    private void upsertDefaultUser(String dni, String nombreCompleto, String email, String password, Usuario.Rol rol) {
        try {
            // Buscar primero por email (es unique y lo más probable)
            Usuario usuario = usuarioRepository.findByEmail(email);

            // Si no existe por email, buscar por DNI
            if (usuario == null) {
                usuario = usuarioRepository.findByDni(dni);
            }

            // Si no existe en absoluto, crear nuevo
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
            log.info("Usuario por defecto asegurado: {} ({})", email, rol);
        } catch (Exception e) {
            log.warn("No se pudo asegurar usuario por defecto {}: {}", email, e.getMessage());
        }
    }
}

