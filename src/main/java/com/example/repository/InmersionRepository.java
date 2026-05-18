package com.example.repository;

import com.example.model.Inmersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InmersionRepository extends JpaRepository<Inmersion, Integer> {

    // Buscar una inmersión por nombre exacto
    Inmersion findByNombre(String nombre);

    // Buscar inmersiones que contengan parte del nombre (más útil)
    List<Inmersion> findByNombreContainingIgnoreCase(String nombre);
}

