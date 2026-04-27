package com.example.demo.model;
import jakarta.persistence.*;

// @Entity le dice a JPA que esta clase es una tabla en la base de datos.
// Hibernate creará automáticamente la tabla "usuario" si no existe.
@Entity
public class Usuario {
    // @Id marca este campo como la CLAVE PRIMARIA de la tabla.
    // @GeneratedValue con IDENTITY delega el autoincremento a la base de datos
    // (equivale a AUTO_INCREMENT en MariaDB). No necesitamos asignarlo manualmente.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Columna "nombre" en la tabla. Spring/Hibernate la crea por su nombre de campo.
    private String nombre;
    // Columna "email" en la tabla. Igual que nombre, se mapea automáticamente.
    private String email;
    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nom) { this.nombre = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}