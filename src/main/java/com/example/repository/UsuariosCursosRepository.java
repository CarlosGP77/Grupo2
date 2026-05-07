package com.example.repository;

import com.example.model.UsuariosCursos;
import com.example.model.UsuariosCursosId;
import com.example.model.Usuario;
import com.example.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuariosCursosRepository extends JpaRepository<UsuariosCursos, UsuariosCursosId> {
    List<UsuariosCursos> findByUsuario(Usuario usuario);
    List<UsuariosCursos> findByCurso(Curso curso);
}

