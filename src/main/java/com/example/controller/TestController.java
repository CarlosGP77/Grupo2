package com.example.controller;

import com.example.model.*;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Controller
public class TestController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private CursoRepository cursoRepository;
    @Autowired private ReservaRepository reservaRepository;
    @Autowired private UsuariosCursosRepository usuariosCursosRepository;
    @Autowired private ActividadRepository actividadRepository;
    @Autowired private UbicacionRepository ubicacionRepository;
    @Autowired private InstructorRepository instructorRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/test")
    public String index(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("cursos", cursoRepository.findAll());
        model.addAttribute("reservas", reservaRepository.findAll());
        model.addAttribute("usuariosCursos", usuariosCursosRepository.findAll());
        model.addAttribute("actividades", actividadRepository.findAll());
        model.addAttribute("ubicaciones", ubicacionRepository.findAll());
        model.addAttribute("instructores", instructorRepository.findAll());
        return "test/index";
    }

    // USUARIO - Formulario
    @PostMapping("/test/usuario/crear")
    public String crearUsuario(@RequestParam String dni,
                               @RequestParam String nombre_completo,
                               @RequestParam String email,
                               @RequestParam String licencia,
                               @RequestParam String password) {
        if (dni != null && !dni.isEmpty()) {
            Usuario u = new Usuario();
            u.setDni(dni);
            u.setNombre_completo(nombre_completo);
            u.setEmail(email);
            u.setLicencia(licencia);
            u.setPassword(passwordEncoder.encode(password));
            usuarioRepository.save(u);
        }
        return "redirect:/test";
    }

    // CURSO - Formulario
    @PostMapping("/test/curso/crear")
    public String crearCurso(@RequestParam String nombre,
                             @RequestParam String descripcion) {
        Curso c = new Curso();
        c.setNombre(nombre);
        c.setDescripcion(descripcion);
        cursoRepository.save(c);
        return "redirect:/test";
    }

    // ACTIVIDAD - Formulario
    @PostMapping("/test/actividad/crear")
    public String crearActividad(@RequestParam String nombre,
                                 @RequestParam String descripcion) {
        Actividad a = new Actividad();
        a.setNombre(nombre);
        a.setDescripcion(descripcion);
        actividadRepository.save(a);
        return "redirect:/test";
    }

    // UBICACION - Formulario
    @PostMapping("/test/ubicacion/crear")
    public String crearUbicacion(@RequestParam String nombre,
                                 @RequestParam String descripcion,
                                 @RequestParam(required = false) Integer actividad_id) {
        Ubicacion u = new Ubicacion();
        u.setNombre(nombre);
        u.setDescripcion(descripcion);
        if (actividad_id != null && actividad_id > 0) {
            Actividad a = actividadRepository.findById(actividad_id).orElse(null);
            u.setActividad(a);
        }
        ubicacionRepository.save(u);
        return "redirect:/test";
    }

    // INSTRUCTOR - Formulario
    @PostMapping("/test/instructor/crear")
    public String crearInstructor(@RequestParam String dni,
                                  @RequestParam String nombre,
                                  @RequestParam String email,
                                  @RequestParam String disponibilidad) {
        Instructor i = new Instructor();
        i.setDni(dni);
        i.setNombre(nombre);
        i.setEmail(email);
        i.setDisponibilidad(disponibilidad.equals("on") || disponibilidad.equals("true"));
        instructorRepository.save(i);
        return "redirect:/test";
    }

    // RESERVA - Formulario
    @PostMapping("/test/reserva/crear")
    public String crearReserva(@RequestParam String dni_usuario,
                               @RequestParam Integer id_curso,
                               @RequestParam String estado) {
        Usuario u = usuarioRepository.findById(dni_usuario).orElse(null);
        Curso c = cursoRepository.findById(id_curso).orElse(null);
        if (u != null && c != null) {
            Reserva r = new Reserva(u, c);
            r.setEstado(estado);
            r.setFecha_hora(LocalDateTime.now());
            reservaRepository.save(r);
        }
        return "redirect:/test";
    }

    @GetMapping("/test/clear-all")
    public String clearAll() {
        reservaRepository.deleteAll();
        usuariosCursosRepository.deleteAll();
        ubicacionRepository.deleteAll();
        actividadRepository.deleteAll();
        instructorRepository.deleteAll();
        cursoRepository.deleteAll();
        usuarioRepository.deleteAll();
        return "redirect:/test";
    }
}

