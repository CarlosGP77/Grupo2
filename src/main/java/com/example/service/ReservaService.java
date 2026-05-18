package com.example.service;

import com.example.model.Reserva;
import com.example.model.Usuario;
import com.example.model.Actividad;
import com.example.model.Ubicacion;
import com.example.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@SuppressWarnings("unused")
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    // Obtener todas las reservas
    public List<Reserva> obtenerTodas() {
        return reservaRepository.findAll();
    }

    // Obtener una reserva por ID
    public Optional<Reserva> obtenerPorId(Integer id) {
        return reservaRepository.findById(id);
    }

    // Obtener reservas de un usuario
    public List<Reserva> obtenerPorUsuario(Usuario usuario) {
        return reservaRepository.findByUsuario(usuario);
    }

    // Obtener reservas de una actividad
    public List<Reserva> obtenerPorActividad(Actividad actividad) {
        return reservaRepository.findByActividad(actividad);
    }

    // Obtener reservas de una ubicación
    public List<Reserva> obtenerPorUbicacion(Ubicacion ubicacion) {
        return reservaRepository.findByUbicacion(ubicacion);
    }

    // Crear una nueva reserva
    public Reserva crear(Reserva reserva) {
        if (reserva.getFecha_inicio() == null) {
            reserva.setFecha_inicio(LocalDateTime.now());
        }
        if (reserva.getPrecio() == null && reserva.getActividad() != null) {
            reserva.setPrecio(reserva.getActividad().getPrecio());
        }
        return reservaRepository.save(reserva);
    }

    // Actualizar una reserva
    public Reserva actualizar(Integer id, Reserva reservaActualizada) {
        Optional<Reserva> reserva = reservaRepository.findById(id);
        if (reserva.isPresent()) {
            Reserva r = reserva.get();
            if (reservaActualizada.getActividad() != null) {
                r.setActividad(reservaActualizada.getActividad());
            }
            if (reservaActualizada.getUbicacion() != null) {
                r.setUbicacion(reservaActualizada.getUbicacion());
            }
            if (reservaActualizada.getPrecio() != null) {
                r.setPrecio(reservaActualizada.getPrecio());
            }
            if (reservaActualizada.getFecha_inicio() != null) {
                r.setFecha_inicio(reservaActualizada.getFecha_inicio());
            }
            if (reservaActualizada.getFecha_fin() != null) {
                r.setFecha_fin(reservaActualizada.getFecha_fin());
            }
            return reservaRepository.save(r);
        }
        return null;
    }

    // Eliminar una reserva
    public void eliminar(Integer id) {
        reservaRepository.deleteById(id);
    }

    // Contar total de reservas
    public long contar() {
        return reservaRepository.count();
    }

    // Contar reservas de un usuario
    public long contarPorUsuario(Usuario usuario) {
        return reservaRepository.countByUsuario(usuario);
    }
}

