package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "instructores_reservas")
public class InstructoresReservas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_instructores_curso")
    private Integer id_instructores_curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructores_dni", referencedColumnName = "dni")
    private Instructor instructor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso", referencedColumnName = "id_curso")
    private Curso curso;
    private LocalDateTime fecha_inicio;
    private LocalDateTime fecha_fin;

    public Integer getId_instructores_curso() { return id_instructores_curso; }
    public void setId_instructores_curso(Integer id_instructores_curso) { this.id_instructores_curso = id_instructores_curso; }
    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }
    public LocalDateTime getFecha_inicio() { return fecha_inicio; }
    public void setFecha_inicio(LocalDateTime fecha_inicio) { this.fecha_inicio = fecha_inicio; }
    public LocalDateTime getFecha_fin() { return fecha_fin; }
    public void setFecha_fin(LocalDateTime fecha_fin) { this.fecha_fin = fecha_fin; }
}

