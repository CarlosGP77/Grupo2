package com.example.model;

import jakarta.persistence.*;
import java.text.Normalizer;
import java.util.Locale;

@Entity
@Table(name = "inmersiones")
@SuppressWarnings("unused")
public class Inmersion {

    public enum Dificultad {
        Baja,
        Media,
        Alta
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inmersion")
    private Integer id_inmersion;

    @Column(length = 150)
    private String nombre;

    // Mapea a la columna "contenido"
    @Column(name = "contenido", columnDefinition = "TEXT")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String datos;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Dificultad dificultad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ubicacion", referencedColumnName = "id_ubicacion")
    private Ubicacion ubicacion;

    public Integer getId_inmersion() { return id_inmersion; }
    public void setId_inmersion(Integer id_inmersion) { this.id_inmersion = id_inmersion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDatos() { return datos; }
    public void setDatos(String datos) { this.datos = datos; }

    public Dificultad getDificultad() { return dificultad; }
    public void setDificultad(Dificultad dificultad) { this.dificultad = dificultad; }

    public Ubicacion getUbicacion() { return ubicacion; }
    public void setUbicacion(Ubicacion ubicacion) { this.ubicacion = ubicacion; }

    @Transient
    public String getNombreArchivoWebp() {
        if (nombre == null || nombre.isBlank()) {
            return "default";
        }

        String normalizado = Normalizer.normalize(nombre, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");

        return normalizado.isBlank() ? "default" : normalizado;
    }
}

