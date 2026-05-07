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
    public CommandLineRunner createAdmin(@Autowired UsuarioRepository usuarioRepository, @Autowired PasswordEncoder encoder) {
        return args -> {
            String adminDni = "admin";
            if (!usuarioRepository.existsById(adminDni)) {
                Usuario u = new Usuario();
                u.setDni(adminDni);
                u.setNombre_completo("Administrador");
                u.setEmail("admin@example.com");
                u.setLicencia("ADMIN");
                u.setPassword(encoder.encode("admin123"));
                usuarioRepository.save(u);
                System.out.println("Usuario admin creado: dni=admin password=admin123");
            }
        };
    }
}