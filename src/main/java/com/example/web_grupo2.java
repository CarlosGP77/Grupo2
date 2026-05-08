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

    @Bean
    public CommandLineRunner createRoles(@Autowired UsuarioRepository usuarioRepository, @Autowired PasswordEncoder encoder) {
        return args -> {
            // Crear usuario ADMIN
            String adminDni = "admin";
            if (!usuarioRepository.existsById(adminDni)) {
                Usuario admin = new Usuario();
                admin.setDni(adminDni);
                admin.setNombre_completo("Administrador");
                admin.setEmail("admin@example.com");
                admin.setLicencia("ADMIN");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRol(Usuario.Rol.ADMIN);
                admin.setVerificar_titulacion(true);  // Admin siempre verificado
                usuarioRepository.save(admin);
                System.out.println("✓ Usuario ADMIN creado: dni=admin password=admin123");
            }

            // Crear usuario VERIFICADOR
            String verDni = "verificador";
            if (!usuarioRepository.existsById(verDni)) {
                Usuario verificador = new Usuario();
                verificador.setDni(verDni);
                verificador.setNombre_completo("Verificador de Credenciales");
                verificador.setEmail("verificador@example.com");
                verificador.setLicencia("VERIFICADOR");
                verificador.setPassword(encoder.encode("verificador123"));
                verificador.setRol(Usuario.Rol.VERIFICADOR);
                verificador.setVerificar_titulacion(true);  // Verificador siempre verificado
                usuarioRepository.save(verificador);
                System.out.println("✓ Usuario VERIFICADOR creado: dni=verificador password=verificador123");
            }
        };
    }
}

