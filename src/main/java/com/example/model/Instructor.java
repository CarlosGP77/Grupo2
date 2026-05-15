package com.example.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instructores")
@SuppressWarnings("unused")
public class Instructor {

    @Id
    @Column(length = 9, unique = true)
    private String dni;
    @Column(length = 100)
    private String nombre;
    @Column(columnDefinition = "TINYINT(1)")
    private Boolean disponibilidad;
    @Column(columnDefinition = "TEXT")
    private String titulaciones;
    @Column(length = 100)
    private String email;
    @Column(name = "telefono_contacto")
    private Integer telefono_contacto;
    @Column(name = "telefono_personal")
    private Integer telefono_personal;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstructoresReservas> instructoresReservas = new ArrayList<>();

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Boolean getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(Boolean disponibilidad) { this.disponibilidad = disponibilidad; }
    public String getTitulaciones() { return titulaciones; }
    public void setTitulaciones(String titulaciones) { this.titulaciones = titulaciones; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getTelefono_contacto() { return telefono_contacto; }
    public void setTelefono_contacto(Integer telefono_contacto) { this.telefono_contacto = telefono_contacto; }
    public Integer getTelefono_personal() { return telefono_personal; }
    public void setTelefono_personal(Integer telefono_personal) { this.telefono_personal = telefono_personal; }

    public List<InstructoresReservas> getInstructoresReservas() { return instructoresReservas; }
    public void setInstructoresReservas(List<InstructoresReservas> instructoresReservas) { this.instructoresReservas = instructoresReservas; }
}

