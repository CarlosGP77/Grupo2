package com.example.service;

import com.example.model.Reservas;
import com.example.model.Usuario;
import com.example.model.Curso;
import com.example.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    // Obtener todas las reservas
    public List<Reservas> obtenerTodas() {
        return reservaRepository.findAll();
    }

    // Obtener una reserva por ID
    public Optional<Reservas> obtenerPorId(Long id) {
        return reservaRepository.findById(id);
    }

    // Obtener reservas de un usuario
    public List<Reservas> obtenerPorUsuario(Usuario usuario) {
        return reservaRepository.findByUsuario(usuario);
    }

    // Obtener reservas de un curso
    public List<Reservas> obtenerPorCurso(Curso curso) {
        return reservaRepository.findByCurso(curso);
    }

    // Obtener reservas por estado
    public List<Reservas> obtenerPorEstado(String estado) {
        return reservaRepository.findByEstado(estado);
    }

    // Crear una nueva reserva
    public Reservas crear(Reservas reserva) {
        reserva.setFecha_hora(LocalDateTime.now());
        reserva.setEstado("pendiente");
        return reservaRepository.save(reserva);
    }

    // Confirmar una reserva
    public Reservas confirmar(Long id) {
        Optional<Reservas> reserva = reservaRepository.findById(id);
        if (reserva.isPresent()) {
            Reservas r = reserva.get();
            r.setEstado("confirmada");
            return reservaRepository.save(r);
        }
        return null;
    }

    // Cancelar una reserva
    public Reservas cancelar(Long id) {
        Optional<Reservas> reserva = reservaRepository.findById(id);
        if (reserva.isPresent()) {
            Reservas r = reserva.get();
            r.setEstado("cancelada");
            return reservaRepository.save(r);
        }
        return null;
    }

    // Actualizar una reserva
    public Reservas actualizar(Long id, Reservas reservaActualizada) {
        Optional<Reservas> reserva = reservaRepository.findById(id);
        if (reserva.isPresent()) {
            Reservas r = reserva.get();
            r.setEstado(reservaActualizada.getEstado());
            return reservaRepository.save(r);
        }
        return null;
    }

    // Eliminar una reserva
    public void eliminar(Long id) {
        reservaRepository.deleteById(id);
    }

    // Contar total de reservas
    public long contar() {
        return reservaRepository.count();
    }
}

