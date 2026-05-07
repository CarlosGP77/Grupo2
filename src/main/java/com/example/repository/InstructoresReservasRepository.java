package com.example.repository;

import com.example.model.InstructoresReservas;
import com.example.model.Instructor;
import com.example.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructoresReservasRepository extends JpaRepository<InstructoresReservas, Integer> {
    List<InstructoresReservas> findByInstructor(Instructor instructor);
    List<InstructoresReservas> findByCurso(Curso curso);
}

