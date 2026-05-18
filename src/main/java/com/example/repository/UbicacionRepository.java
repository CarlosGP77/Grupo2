package com.example.repository;

import com.example.model.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Integer> {

    // Buscar ubicación por nombre exacto
    Ubicacion findByNombre(String nombre);

    // Buscar ubicaciones por coincidencia parcial
    List<Ubicacion> findByNombreContainingIgnoreCase(String nombre);
}


