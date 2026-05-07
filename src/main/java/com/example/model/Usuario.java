package com.example.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @Column(length = 9)
    private String dni;
    @Column(length = 100)
    private String nombre_completo;
    @Column(length = 100)
    private String email;
    @Column(length = 16)
    private String licencia;
    @Column(columnDefinition = "TEXT")
    private String titulaciones;
    @Column(length = 15)
    private String poliza_seguro;
    @Column
    private Integer telefono;
    @Column(name = "telefono_de_contacto")
    private Integer telefono_contacto;
    private String password;
    @Column(name = "Verificar_titulacion")
    private boolean  verificar_titulacion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas = new ArrayList<>();
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuariosCursos> usuariosCursos = new ArrayList<>();

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombre_completo() { return nombre_completo; }
    public void setNombre_completo(String nombre_completo) { this.nombre_completo = nombre_completo; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getLicencia() { return licencia; }
    public void setLicencia(String licencia) { this.licencia = licencia; }
    public String getTitulaciones() { return titulaciones; }
    public void setTitulaciones(String titulaciones) { this.titulaciones = titulaciones; }
    public String getPoliza_seguro() { return poliza_seguro; }
    public void setPoliza_seguro(String poliza_seguro) { this.poliza_seguro = poliza_seguro; }
    public Integer getTelefono() { return telefono; }
    public void setTelefono(Integer telefono) { this.telefono = telefono; }
    public Integer getTelefono_contacto() { return telefono_contacto; }
    public void setTelefono_contacto(Integer telefono_contacto) { this.telefono_contacto = telefono_contacto; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean getVerificar_titulacion() { return verificar_titulacion; }
    public void setVerificar_titulacion(boolean verificar_titulacion) {}



    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }
    public List<UsuariosCursos> getUsuariosCursos() { return usuariosCursos; }
    public void setUsuariosCursos(List<UsuariosCursos> usuariosCursos) { this.usuariosCursos = usuariosCursos; }
}
