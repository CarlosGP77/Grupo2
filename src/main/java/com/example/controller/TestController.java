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
import org.springframework.security.crypto.password.PasswordEncoder;

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
                               @RequestParam String password,
                               @RequestParam(required = false, defaultValue = "USUARIO") String rol) {
        if (dni != null && !dni.isEmpty()) {
            Usuario u = new Usuario();
            u.setDni(dni);
            u.setNombre_completo(nombre_completo);
            u.setEmail(email);
            u.setLicencia(licencia);
            u.setPassword(passwordEncoder.encode(password));
            
            // Asignar rol (ADMIN, VERIFICADOR, USUARIO)
            try {
                u.setRol(Usuario.Rol.valueOf(rol.toUpperCase()));
            } catch (IllegalArgumentException e) {
                u.setRol(Usuario.Rol.USUARIO);
            }
            
            // Los usuarios nuevos por defecto no están verificados
            u.setVerificar_titulacion(false);
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
                                 @RequestParam String descripcion) {
        Ubicacion u = new Ubicacion();
        u.setNombre(nombre);
        u.setDescripcion(descripcion);
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
    public String crearReserva(@RequestParam Integer usuario_id,
                               @RequestParam Integer actividad_id,
                               @RequestParam Integer ubicacion_id) {
        Usuario u = usuarioRepository.findById(usuario_id).orElse(null);
        Actividad a = actividadRepository.findById(actividad_id).orElse(null);
        Ubicacion loc = ubicacionRepository.findById(ubicacion_id).orElse(null);
        if (u != null && a != null && loc != null) {
            Reserva r = new Reserva(u, a, loc);
            r.setFecha_inicio(LocalDateTime.now());
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

