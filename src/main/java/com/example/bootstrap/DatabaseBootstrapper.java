package com.example.bootstrap;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseBootstrapper implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseBootstrapper.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseBootstrapper(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        ensureUser(
                "12345678A",
                "Administrador del Sistema",
                "admin@example.com",
                "Admin_123",
                Usuario.Rol.ADMIN,
                true
        );

        ensureUser(
                "87654321B",
                "Verificador de Credenciales",
                "verificador@example.com",
                "Admin_123",
                Usuario.Rol.VERIFICADOR,
                true
        );
    }

    private void ensureUser(String dni,
                            String nombreCompleto,
                            String email,
                            String rawPassword,
                            Usuario.Rol rol,
                            boolean verificado) {
        if (usuarioRepository.findByEmail(email) != null) {
            log.info("Usuario con email {} ya existe; se omite la creación automática.", email);
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setDni(dni);
        usuario.setNombre_completo(nombreCompleto);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(rawPassword));
        usuario.setRol(rol);
        usuario.setVerificar_titulacion(verificado);
        usuarioRepository.save(usuario);

        log.info("Usuario {} ({}) creado automáticamente.", nombreCompleto, email);
    }
}

