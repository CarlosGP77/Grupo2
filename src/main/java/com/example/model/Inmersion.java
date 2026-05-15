package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inmersiones")
@SuppressWarnings("unused")
public class Inmersion {

    public enum Dificultad {
        BAJA,
        MEDIA,
        ALTA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inmersion")
    private Integer id_inmersion;

    @Column(length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    @Column(columnDefinition = "TEXT")
    private String datos;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Dificultad dificultad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacion", referencedColumnName = "id_ubicacion")
    private Ubicacion ubicacion;

    public Integer getId_inmersion() { return id_inmersion; }
    public void setId_inmersion(Integer id_inmersion) { this.id_inmersion = id_inmersion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getDatos() { return datos; }
    public void setDatos(String datos) { this.datos = datos; }

    public Dificultad getDificultad() { return dificultad; }
    public void setDificultad(Dificultad dificultad) { this.dificultad = dificultad; }

    public Ubicacion getUbicacion() { return ubicacion; }
    public void setUbicacion(Ubicacion ubicacion) { this.ubicacion = ubicacion; }
}

