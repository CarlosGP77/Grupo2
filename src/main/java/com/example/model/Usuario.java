package com.example.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@SuppressWarnings("unused")
public class Usuario {

    public enum Rol {
        ADMIN,        // Control total
        VERIFICADOR,  // Solo verifica credenciales
        USUARIO       // Usuario normal
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id_usuario;

    @Column(length = 9, unique = true)
    private String dni;
    @Column(length = 100)
    private String nombre_completo;
    @Column(length = 100, unique = true)
    private String email;
    @Column(length = 50)
    private String licencia;
    @Column(columnDefinition = "TEXT")
    private String titulaciones;
    @Column(length = 50)
    private String poliza_seguro;
    @Column(length = 20)
    private String telefono;
    @Column(length = 20)
    private String telefono_contacto;
    private String password;
    @Column(name = "Verificar_titulacion")
    private Boolean verificar_titulacion = false;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Rol rol = Rol.USUARIO;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas = new ArrayList<>();

    public Integer getId_usuario() { return id_usuario; }
    public void setId_usuario(Integer id_usuario) { this.id_usuario = id_usuario; }

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
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getTelefono_contacto() { return telefono_contacto; }
    public void setTelefono_contacto(String telefono_contacto) { this.telefono_contacto = telefono_contacto; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean getVerificar_titulacion() { return verificar_titulacion; }
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public void setVerificar_titulacion(boolean verificar_titulacion) { this.verificar_titulacion = verificar_titulacion; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }
}
