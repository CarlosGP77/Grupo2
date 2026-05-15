package com.example.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "curso")
@SuppressWarnings("unused")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Integer id_curso;

    @Column(length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuariosCursos> usuariosCursos = new ArrayList<>();

    public Integer getId_curso() { return id_curso; }
    public void setId_curso(Integer id_curso) { this.id_curso = id_curso; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<UsuariosCursos> getUsuariosCursos() { return usuariosCursos; }
    public void setUsuariosCursos(List<UsuariosCursos> usuariosCursos) { this.usuariosCursos = usuariosCursos; }
}

