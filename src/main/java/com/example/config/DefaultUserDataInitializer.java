package com.example.config;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DefaultUserDataInitializer implements CommandLineRunner {

    private static final String DEFAULT_PASSWORD_HASH = "$2a$10$slYQmyNdGzin5FEKgXNJqOPt3qhw4dVB3nZJUSXv.1OrmF8qkFLne";

    private final UsuarioRepository usuarioRepository;
    public DefaultUserDataInitializer(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        upsertDefaultUser(
                "admin",
                "Administrador del Sistema",
                "admin@mourosub.com",
                Usuario.Rol.ADMIN
        );

        upsertDefaultUser(
                "verificador",
                "Verificador de Credenciales",
                "verificador@mourosub.com",
                Usuario.Rol.VERIFICADOR
        );
    }

    private void upsertDefaultUser(String dni, String nombreCompleto, String email, Usuario.Rol rol) {
        Usuario usuario = usuarioRepository.findByDni(dni);
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setDni(dni);
        }

        usuario.setNombre_completo(nombreCompleto);
        usuario.setEmail(email);
        usuario.setPassword(resolvePasswordHash());
        usuario.setRol(rol);
        usuario.setVerificar_titulacion(true);

        usuarioRepository.save(usuario);
        log.info("Usuario por defecto asegurado: {} ({})", email, rol);
    }

    private String resolvePasswordHash() {
        return DEFAULT_PASSWORD_HASH;
    }
}

