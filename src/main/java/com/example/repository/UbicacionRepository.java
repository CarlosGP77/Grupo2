package com.example.repository;

import com.example.model.Ubicacion;
import com.example.model.Curso;
import com.example.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Integer> {
    List<Ubicacion> findByCurso(Curso curso);
    List<Ubicacion> findByActividad(Actividad actividad);
}

