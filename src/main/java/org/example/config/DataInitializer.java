package org.example.config;

import org.example.model.*;
import org.example.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(
            UserService userService,
            LocationService locationService,
            CourseService courseService,
            ActivityService activityService,
            PasswordEncoder passwordEncoder) {

        return args -> {
            // Check if data already exists
            if (userService.getTotalUsers() > 0) {
                return; // Data already initialized
            }

            // Create Admin User
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@example.com")
                    .firstName("Admin")
                    .lastName("User")
                    .role(UserRole.ADMIN)
                    .verificationStatus(VerificationStatus.VERIFIED)
                    .enabled(true)
                    .createdAt(LocalDateTime.now())
                    .build();
            userService.saveUser(admin);

            // Create Verificador User
            User verificador = User.builder()
                    .username("verificador")
                    .password(passwordEncoder.encode("verificador123"))
                    .email("verificador@example.com")
                    .firstName("Verificador")
                    .lastName("User")
                    .role(UserRole.VERIFICADOR)
                    .verificationStatus(VerificationStatus.VERIFIED)
                    .enabled(true)
                    .createdAt(LocalDateTime.now())
                    .build();
            userService.saveUser(verificador);

            // Create Regular User 1
            User usuario1 = User.builder()
                    .username("usuario1")
                    .password(passwordEncoder.encode("usuario123"))
                    .email("usuario1@example.com")
                    .firstName("Juan")
                    .lastName("Pérez")
                    .role(UserRole.USUARIO)
                    .verificationStatus(VerificationStatus.PENDING)
                    .enabled(true)
                    .createdAt(LocalDateTime.now())
                    .build();
            userService.saveUser(usuario1);

            // Create Regular User 2
            User usuario2 = User.builder()
                    .username("usuario2")
                    .password(passwordEncoder.encode("usuario123"))
                    .email("usuario2@example.com")
                    .firstName("María")
                    .lastName("García")
                    .role(UserRole.USUARIO)
                    .verificationStatus(VerificationStatus.VERIFIED)
                    .enabled(true)
                    .createdAt(LocalDateTime.now())
                    .build();
            userService.saveUser(usuario2);

            // Create Locations
            Location location1 = locationService.createLocation(
                    "Sala de Conferencias A",
                    "Sala equipada con proyector y asientos para 50 personas",
                    "Calle Principal 123",
                    "Madrid",
                    "28001",
                    50.0
            );

            Location location2 = locationService.createLocation(
                    "Aula de Informática",
                    "Aula con 30 ordenadores para prácticas",
                    "Calle Secundaria 456",
                    "Barcelona",
                    "08002",
                    30.0
            );

            // Create Courses
            Course course1 = courseService.createCourse(
                    "JAVA001",
                    "Programación en Java",
                    "Curso introductorio de Java para principiantes",
                    40,
                    100.0
            );

            Course course2 = courseService.createCourse(
                    "SPRING001",
                    "Spring Boot Avanzado",
                    "Aprende a desarrollar aplicaciones con Spring Boot",
                    50,
                    150.0
            );

            // Create Activities
            Activity activity1 = activityService.createActivity(
                    "Sesión 1: Introducción",
                    "Primera sesión del curso",
                    LocalDateTime.now().plusDays(5),
                    LocalDateTime.now().plusDays(5).plusHours(2),
                    50,
                    course1,
                    location1
            );

            Activity activity2 = activityService.createActivity(
                    "Sesión 2: Conceptos Avanzados",
                    "Segunda sesión del curso",
                    LocalDateTime.now().plusDays(12),
                    LocalDateTime.now().plusDays(12).plusHours(2),
                    50,
                    course1,
                    location1
            );

            Activity activity3 = activityService.createActivity(
                    "Workshop Práctico",
                    "Taller práctico de Spring Boot",
                    LocalDateTime.now().plusDays(15),
                    LocalDateTime.now().plusDays(15).plusHours(3),
                    30,
                    course2,
                    location2
            );
        };
    }
}

