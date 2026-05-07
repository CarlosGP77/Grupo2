package com.example.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "curso")
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
    private List<Ubicacion> ubicaciones = new ArrayList<>();
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuariosCursos> usuariosCursos = new ArrayList<>();
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas = new ArrayList<>();

    public Integer getId_curso() { return id_curso; }
    public void setId_curso(Integer id_curso) { this.id_curso = id_curso; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<Ubicacion> getUbicaciones() { return ubicaciones; }
    public void setUbicaciones(List<Ubicacion> ubicaciones) { this.ubicaciones = ubicaciones; }
    public List<UsuariosCursos> getUsuariosCursos() { return usuariosCursos; }
    public void setUsuariosCursos(List<UsuariosCursos> usuariosCursos) { this.usuariosCursos = usuariosCursos; }
    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }
}

