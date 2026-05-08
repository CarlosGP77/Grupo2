package com.example.repository;

import com.example.model.Reservas;
import com.example.model.Usuario;
import com.example.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reservas, Long> {
    List<Reservas> findByUsuario(Usuario usuario);
    List<Reservas> findByCurso(Curso curso);
    List<Reservas> findByEstado(String estado);
}

