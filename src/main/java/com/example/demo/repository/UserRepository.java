package com.example.demo.repository;

import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
// Los dos parámetros genéricos indican:
//   - Usuario → la entidad sobre la que opera
//   - Long    → el tipo del campo @Id de esa entidad
public interface UserRepository extends JpaRepository<Usuario, Long> {
}