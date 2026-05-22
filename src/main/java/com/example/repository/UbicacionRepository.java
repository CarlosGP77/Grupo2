package com.example.repository;

import com.example.model.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Integer> {

    // Buscar ubicación por nombre exacto
    Ubicacion findByNombre(String nombre);

    @Query("select distinct u from Ubicacion u left join fetch u.inmersiones")
    List<Ubicacion> findAllWithInmersiones();

    @Query("select distinct u from Ubicacion u left join fetch u.inmersiones where u.id_ubicacion = ?1")
    Optional<Ubicacion> findByIdWithInmersiones(Integer id);

    // Buscar ubicaciones por coincidencia parcial
    List<Ubicacion> findByNombreContainingIgnoreCase(String nombre);
}


