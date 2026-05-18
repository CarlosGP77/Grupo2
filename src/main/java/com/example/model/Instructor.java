package com.example.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instructores")
@SuppressWarnings("unused")
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_instructor")
    private Integer id_instructor;

    @Column(length = 9, unique = true)
    private String dni;
    @Column(length = 150)
    private String nombre;
    @Column(columnDefinition = "BOOLEAN")
    private Boolean disponibilidad = true;
    @Column(columnDefinition = "TEXT")
    private String titulaciones;
    @Column(length = 100, unique = true)
    private String email;
    @Column(length = 20)
    private String telefono_contacto;
    @Column(length = 20)
    private String telefono_personal;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstructoresReservas> instructoresReservas = new ArrayList<>();

    public Integer getId_instructor() { return id_instructor; }
    public void setId_instructor(Integer id_instructor) { this.id_instructor = id_instructor; }

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
    public String getTelefono_contacto() { return telefono_contacto; }
    public void setTelefono_contacto(String telefono_contacto) { this.telefono_contacto = telefono_contacto; }
    public String getTelefono_personal() { return telefono_personal; }
    public void setTelefono_personal(String telefono_personal) { this.telefono_personal = telefono_personal; }

    public List<InstructoresReservas> getInstructoresReservas() { return instructoresReservas; }
    public void setInstructoresReservas(List<InstructoresReservas> instructoresReservas) { this.instructoresReservas = instructoresReservas; }
}

