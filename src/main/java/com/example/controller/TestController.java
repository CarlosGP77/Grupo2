package com.example.controller;

import com.example.model.Curso;
import com.example.model.Reservas;
import com.example.model.Usuario;
import com.example.model.UsuariosCursos;
import com.example.repository.CursoRepository;
import com.example.repository.ReservaRepository;
import com.example.repository.UsuarioRepository;
import com.example.repository.UsuariosCursosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Controller
public class TestController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuariosCursosRepository usuariosCursosRepository;

    @GetMapping("/test")
    public String index(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Curso> cursos = cursoRepository.findAll();
        List<Reservas> reservas = reservaRepository.findAll();
        List<UsuariosCursos> usuariosCursos = usuariosCursosRepository.findAll();

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("cursos", cursos);
        model.addAttribute("reservas", reservas);
        model.addAttribute("usuariosCursos", usuariosCursos);
        return "test/index";
    }

    @GetMapping("/test/create-user")
    public String createUser(@RequestParam(required = false) String dni,
                             @RequestParam(required = false) String password) {
        String id = (dni == null || dni.isEmpty()) ? "000000001" : dni;
        if (!usuarioRepository.existsById(id)) {
            Usuario u = new Usuario();
            u.setDni(id);
            u.setNombre_completo("Usuario " + id);
            u.setEmail("user" + id + "@example.com");
            u.setLicencia("LIC-" + id);
            if (password != null && !password.isEmpty()) {
                PasswordEncoder encoder = new BCryptPasswordEncoder();
                u.setPassword(encoder.encode(password));
            }
            usuarioRepository.save(u);
        }
        return "redirect:/test";
    }

    @GetMapping("/test/create-course")
    public String createCourse(@RequestParam(required = false) String name) {
        String nombre = (name == null || name.isEmpty()) ? "Curso demo" : name;
        Curso c = new Curso();
        c.setNombre(nombre);
        c.setDescripcion("Descripción de prueba para " + nombre);
        cursoRepository.save(c);
        return "redirect:/test";
    }

    @GetMapping("/test/create-reserva")
    public String createReserva() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Curso> cursos = cursoRepository.findAll();
        if (!usuarios.isEmpty() && !cursos.isEmpty()) {
            Usuario u = usuarios.get(0);
            Curso c = cursos.get(0);
            Reservas r = new Reservas(u, c);
            r.setEstado("CONFIRMADA");
            r.setFecha_hora(LocalDateTime.now());
            reservaRepository.save(r);
        }
        return "redirect:/test";
    }

    @GetMapping("/test/create-usuarios-cursos")
    public String createUsuariosCursos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Curso> cursos = cursoRepository.findAll();
        if (!usuarios.isEmpty() && !cursos.isEmpty()) {
            Usuario u = usuarios.get(0);
            Curso c = cursos.get(0);
            UsuariosCursos uc = new UsuariosCursos();
            uc.setUsuario(u);
            uc.setCurso(c);
            uc.setPrecio(BigDecimal.ZERO);
            usuariosCursosRepository.save(uc);
        }
        return "redirect:/test";
    }

    @GetMapping("/test/clear-all")
    public String clearAll() {
        // Delete in order to avoid FK constraint issues
        reservaRepository.deleteAll();
        usuariosCursosRepository.deleteAll();
        cursoRepository.deleteAll();
        usuarioRepository.deleteAll();
        return "redirect:/test";
    }
}

