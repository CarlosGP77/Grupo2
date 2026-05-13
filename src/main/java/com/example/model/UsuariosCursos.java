package com.example.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios_cursos")
@IdClass(UsuariosCursosId.class)
public class UsuariosCursos {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarios_dni", referencedColumnName = "dni", columnDefinition = "VARCHAR(9)")
    private Usuario usuario;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso", referencedColumnName = "id_curso")
    private Curso curso;

    @Column(precision = 6, scale = 2)
    private java.math.BigDecimal precio;
    private LocalDateTime fecha_inicio;
    private LocalDateTime fecha_fin;

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }
    public java.math.BigDecimal getPrecio() { return precio; }
    public void setPrecio(java.math.BigDecimal precio) { this.precio = precio; }
    public LocalDateTime getFecha_inicio() { return fecha_inicio; }
    public void setFecha_inicio(LocalDateTime fecha_inicio) { this.fecha_inicio = fecha_inicio; }
    public LocalDateTime getFecha_fin() { return fecha_fin; }
    public void setFecha_fin(LocalDateTime fecha_fin) { this.fecha_fin = fecha_fin; }
}

