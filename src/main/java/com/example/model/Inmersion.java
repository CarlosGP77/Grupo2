package com.example.model;

import jakarta.persistence.*;

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
            return String.valueOf(id_inmersion != null ? id_inmersion : "default");
        }

        // Mapeo explícito basado en palabras clave del nombre
        String nombreLower = nombre.toLowerCase();
        if (nombreLower.contains("costa") && nombreLower.contains("cantabrica")) {
            return "costa-cantabrica";
        }
        if (nombreLower.contains("bajo") && nombreLower.contains("jose")) {
            return "el-bajo-de-jose";
        }
        if (nombreLower.contains("calo") || nombreLower.contains("caló")) {
            return "el-calo";
        }
        if (nombreLower.contains("camello")) {
            return "el-camello";
        }
        if (nombreLower.contains("faro") && nombreLower.contains("cerda")) {
            return "faro-de-la-cerda";
        }
        if (nombreLower.contains("cala")) {
            return "la-cala";
        }
        if (nombreLower.contains("norte")) {
            return "la-norte";
        }
        if (nombreLower.contains("cuevas")) {
            return "las-cuevas";
        }
        if (nombreLower.contains("lastras") || nombreLower.contains("palacio")) {
            return "las-lastras-del-palacio";
        }
        if (nombreLower.contains("puerto") && nombreLower.contains("deportivo")) {
            return "puerto-deportivo-marina-del-cantabrico";
        }

        // Fallback: usar el ID como nombre de archivo
        return String.valueOf(id_inmersion != null ? id_inmersion : "default");
    }
}

