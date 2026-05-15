package com.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer id_reserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dni", referencedColumnName = "dni")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad", referencedColumnName = "id_actividad")
    private Actividad actividad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacion", referencedColumnName = "id_ubicacion")
    private Ubicacion ubicacion;

    @Column(precision = 6, scale = 2)
    private BigDecimal precio;

    @Column(name = "fecha_inicio")
    private LocalDateTime fecha_inicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fecha_fin;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstructoresReservas> instructoresReservas = new ArrayList<>();

    // Constructores
    public Reserva() {}

    public Reserva(Usuario usuario, Actividad actividad, Ubicacion ubicacion) {
        this.usuario = usuario;
        this.actividad = actividad;
        this.ubicacion = ubicacion;
    }

    // Getters y Setters
    public Integer getId_reserva() { return id_reserva; }
    public void setId_reserva(Integer id_reserva) { this.id_reserva = id_reserva; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Actividad getActividad() { return actividad; }
    public void setActividad(Actividad actividad) { this.actividad = actividad; }

    public Ubicacion getUbicacion() { return ubicacion; }
    public void setUbicacion(Ubicacion ubicacion) { this.ubicacion = ubicacion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public LocalDateTime getFecha_inicio() { return fecha_inicio; }
    public void setFecha_inicio(LocalDateTime fecha_inicio) { this.fecha_inicio = fecha_inicio; }

    public LocalDateTime getFecha_fin() { return fecha_fin; }
    public void setFecha_fin(LocalDateTime fecha_fin) { this.fecha_fin = fecha_fin; }

    public List<InstructoresReservas> getInstructoresReservas() { return instructoresReservas; }
    public void setInstructoresReservas(List<InstructoresReservas> instructoresReservas) { this.instructoresReservas = instructoresReservas; }
}
