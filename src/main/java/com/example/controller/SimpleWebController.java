package com.example.controller;

import com.example.model.Actividad;
import com.example.model.Reserva;
import com.example.model.Ubicacion;
import com.example.model.Usuario;
import com.example.repository.ActividadRepository;
import com.example.repository.ReservaRepository;
import com.example.repository.UbicacionRepository;
import com.example.repository.UsuarioRepository;
import com.example.service.ReservaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SimpleWebController {

    private final UsuarioRepository usuarioRepository;
    private final ActividadRepository actividadRepository;
    private final UbicacionRepository ubicacionRepository;
    private final ReservaRepository reservaRepository;
    private final ReservaService reservaService;

    public SimpleWebController(UsuarioRepository usuarioRepository,
                               ActividadRepository actividadRepository,
                               UbicacionRepository ubicacionRepository,
                               ReservaRepository reservaRepository,
                               ReservaService reservaService) {
        this.usuarioRepository = usuarioRepository;
        this.actividadRepository = actividadRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.reservaRepository = reservaRepository;
        this.reservaService = reservaService;
    }

    @GetMapping({"/simple", "/simple/reservas"})
    public String simpleIndex(@RequestParam(required = false) String creada,
                              @RequestParam(required = false) String error,
                              Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("actividades", actividadRepository.findAll());
        model.addAttribute("ubicaciones", ubicacionRepository.findAll());
        model.addAttribute("reservas", reservaRepository.findAll());
        model.addAttribute("creada", creada);
        model.addAttribute("error", error);
        return "simple/index";
    }

    @PostMapping("/simple/reservas")
    public String crearReserva(@RequestParam Integer usuario_id,
                               @RequestParam Integer actividad_id,
                               @RequestParam Integer ubicacion_id) {
        try {
            Usuario usuario = usuarioRepository.getReferenceById(usuario_id);
            Actividad actividad = actividadRepository.getReferenceById(actividad_id);
            Ubicacion ubicacion = ubicacionRepository.getReferenceById(ubicacion_id);

            Reserva reserva = new Reserva(usuario, actividad, ubicacion);
            reservaService.crear(reserva);
            return "redirect:/simple?creada=1";
        } catch (Exception ex) {
            return "redirect:/simple?error=error_creando_reserva";
        }
    }
}

