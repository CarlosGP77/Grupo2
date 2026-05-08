package com.example;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.repository.UsuarioRepository;
import com.example.model.Usuario;

@SpringBootApplication
public class web_grupo2 {
    public static void main(String[] args) {
        SpringApplication.run(web_grupo2.class, args);
    }

    /**
     * Crea usuarios ADMIN y VERIFICADOR solo si no existen en la BD.
     * Una vez creados, se guardan permanentemente.
     * Esto se ejecuta UNA SOLA VEZ en el primer arranque.
     * En arranques posteriores, solo verifica que existan (sin intentar crear de nuevo).
     */
    @Bean
    public CommandLineRunner initializeDefaultUsers(@Autowired UsuarioRepository usuarioRepository,
                                                      @Autowired PasswordEncoder encoder) {
        return args -> {
            // Variables para control
            String adminDni = "admin";
            String verDni = "verificador";

            // ADMIN: Se crea solo si no existe
            if (!usuarioRepository.existsById(adminDni)) {
                Usuario admin = new Usuario();
                admin.setDni(adminDni);
                admin.setNombre_completo("Administrador");
                admin.setEmail("admin@example.com");
                admin.setLicencia("ADMIN");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRol(Usuario.Rol.ADMIN);
                admin.setVerificar_titulacion(true);
                usuarioRepository.save(admin);
                System.out.println("✓ Usuario ADMIN creado en BD: dni=admin password=admin123");
            } else {
                System.out.println("✓ Usuario ADMIN ya existe en BD (sin cambios)");
            }

            // VERIFICADOR: Se crea solo si no existe
            if (!usuarioRepository.existsById(verDni)) {
                Usuario verificador = new Usuario();
                verificador.setDni(verDni);
                verificador.setNombre_completo("Verificador de Credenciales");
                verificador.setEmail("verificador@example.com");
                verificador.setLicencia("VERIFICADOR");
                verificador.setPassword(encoder.encode("verificador123"));
                verificador.setRol(Usuario.Rol.VERIFICADOR);
                verificador.setVerificar_titulacion(true);
                usuarioRepository.save(verificador);
                System.out.println("✓ Usuario VERIFICADOR creado en BD: dni=verificador password=verificador123");
            } else {
                System.out.println("✓ Usuario VERIFICADOR ya existe en BD (sin cambios)");
            }
        };
    }
}

