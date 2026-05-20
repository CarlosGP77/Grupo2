package com.example.repository;

import com.example.model.Inmersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InmersionRepository extends JpaRepository<Inmersion, Integer> {

    @Query("select i from Inmersion i left join fetch i.ubicacion")
    List<Inmersion> findAllWithUbicacion();
    Inmersion findByNombre(String nombre);
    List<Inmersion> findByNombreContainingIgnoreCase(String nombre);
}
