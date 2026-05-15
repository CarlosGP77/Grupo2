package com.example.repository;

import com.example.model.Reserva;
import com.example.model.Usuario;
import com.example.model.Actividad;
import com.example.model.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByUsuario(Usuario usuario);
    List<Reserva> findByActividad(Actividad actividad);
    List<Reserva> findByUbicacion(Ubicacion ubicacion);
    long countByUsuario(Usuario usuario);
}

