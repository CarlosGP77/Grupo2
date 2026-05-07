package com.example.repository;

import com.example.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, String> {
    Instructor findByEmail(String email);
    Instructor findByDni(String dni);
    List<Instructor> findByNombre(String nombre);
    List<Instructor> findByDisponibilidad(Boolean disponibilidad);
}

