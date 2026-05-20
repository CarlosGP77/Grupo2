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
    public List<Reserva> obtenerTodas() {
        return reservaRepository.findAll();
    }
    public Optional<Reserva> obtenerPorId(Integer id) {
        return reservaRepository.findById(id);
    }
    public List<Reserva> obtenerPorUsuario(Usuario usuario) {
        return reservaRepository.findByUsuario(usuario);
    }
    public List<Reserva> obtenerPorActividad(Actividad actividad) {
        return reservaRepository.findByActividad(actividad);
    }
    public List<Reserva> obtenerPorUbicacion(Ubicacion ubicacion) {
        return reservaRepository.findByUbicacion(ubicacion);
    }
    public Reserva crear(Reserva reserva) {
        if (reserva.getFecha_inicio() == null) {
            reserva.setFecha_inicio(LocalDateTime.now());
        }
        if (reserva.getFecha_fin() == null) {
            reserva.setFecha_fin(reserva.getFecha_inicio().plusHours(1));
        }
        if (reserva.getPrecio() == null && reserva.getActividad() != null) {
            reserva.setPrecio(reserva.getActividad().getPrecio());
        }
        return reservaRepository.save(reserva);
    }
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
    public void eliminar(Integer id) {
        reservaRepository.deleteById(id);
    }
    public long contar() {
        return reservaRepository.count();
    }
    public long contarPorUsuario(Usuario usuario) {
        return reservaRepository.countByUsuario(usuario);
    }
}

