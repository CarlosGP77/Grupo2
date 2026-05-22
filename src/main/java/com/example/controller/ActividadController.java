package com.example.controller;

import com.example.model.Actividad;
import com.example.service.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.service.*;
import com.example.model.*;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Random;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @GetMapping("/actividades")
    public String listar(Model model) {
        model.addAttribute("actividades", actividadService.obtenerTodos());
        return "html/actividades";
    }

    @GetMapping("/actividades/{id}")
    public String verActividad(@PathVariable Integer id, Model model) {

        Actividad actividad = actividadService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        model.addAttribute("actividad", actividad);
        return "html/actividad-detalle";
    }

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UbicacionService ubicacionService;


    @PostMapping("/actividades/{id}/reservar")
    public String reservarActividad(
            @PathVariable Integer id,
            Principal principal,
            Model model
    ) {
        Usuario usuario = usuarioService.obtenerPorEmail(principal.getName());
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Actividad actividad = actividadService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setActividad(actividad);

        List<Ubicacion> ubicaciones = ubicacionService.listarTodas();
        Random random = new Random();
        Ubicacion ubicacionRandom = ubicaciones.get(random.nextInt(ubicaciones.size()));
        reserva.setUbicacion(ubicacionRandom);

        reserva.setPrecio(actividad.getPrecio());
        reserva.setFecha_inicio(LocalDateTime.now());
        reserva.setFecha_fin(LocalDateTime.now().plusHours(2));

        reservaService.crear(reserva);

        // 🔥 ENVIAR DATOS AL HTML
        model.addAttribute("actividad", actividad);
        model.addAttribute("precio", reserva.getPrecio());
        model.addAttribute("ubicacion", ubicacionRandom);
        model.addAttribute("fechaInicio", reserva.getFecha_inicio());

        return "html/reservas-confirmacion";
    }
}
