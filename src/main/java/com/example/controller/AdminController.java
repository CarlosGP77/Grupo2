package com.example.controller;

import com.example.model.*;
import com.example.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private CursoRepository cursoRepository;
    @Autowired private ReservaRepository reservaRepository;
    @Autowired private UbicacionRepository ubicacionRepository;
    @Autowired private InmersionRepository inmersionRepository;
    @Autowired private ActividadRepository actividadRepository;
    @Autowired private InstructorRepository instructorRepository;
    @Autowired private InstructoresReservasRepository instructoresReservasRepository;
    @Autowired private UsuariosCursosRepository usuariosCursosRepository;
    @Autowired private WebFooterRepository webFooterRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/panel")
    public String adminPanel(@RequestParam(required = false) Integer editarUsuario,
                             @RequestParam(required = false) Integer editarCurso,
                             @RequestParam(required = false) Integer editarUbicacion,
                             @RequestParam(required = false) Integer editarInmersion,
                             Model model) {
        cargarDashboard(model);
        model.addAttribute("usuarioFormulario", editarUsuario != null ? usuarioRepository.findById(editarUsuario).orElse(new Usuario()) : new Usuario());
        model.addAttribute("cursoFormulario", editarCurso != null ? cursoRepository.findById(editarCurso).orElse(new Curso()) : new Curso());
        model.addAttribute("ubicacionFormulario", editarUbicacion != null ? ubicacionRepository.findById(editarUbicacion).orElse(new Ubicacion()) : new Ubicacion());
        model.addAttribute("inmersionFormulario", editarInmersion != null ? inmersionRepository.findById(editarInmersion).orElse(new Inmersion()) : new Inmersion());
        return "admin/panel";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@RequestParam(required = false) Integer id_usuario,
                                 @RequestParam String dni,
                                 @RequestParam String nombre_completo,
                                 @RequestParam String email,
                                 @RequestParam(required = false) String licencia,
                                 @RequestParam(required = false) String titulaciones,
                                 @RequestParam(required = false) String poliza_seguro,
                                 @RequestParam(required = false) String telefono,
                                 @RequestParam(required = false) String telefono_contacto,
                                 @RequestParam(required = false) String password,
                                 @RequestParam(required = false) String confirmPassword,
                                 @RequestParam(required = false) String rol,
                                 @RequestParam(required = false, defaultValue = "false") boolean verificar_titulacion,
                                 Model model) {
        Usuario usuario = id_usuario != null ? usuarioRepository.findById(id_usuario).orElse(new Usuario()) : new Usuario();
        usuario.setDni(normalizar(dni));
        usuario.setNombre_completo(normalizar(nombre_completo));
        usuario.setEmail(normalizarEmail(email));
        usuario.setLicencia(normalizar(licencia));
        usuario.setTitulaciones(normalizarTexto(titulaciones));
        usuario.setPoliza_seguro(normalizar(poliza_seguro));
        usuario.setTelefono(normalizar(telefono));
        usuario.setTelefono_contacto(normalizar(telefono_contacto));
        usuario.setRol(parseRol(rol, usuario.getRol()));
        usuario.setVerificar_titulacion(verificar_titulacion);

        if (usuario.getDni() == null || usuario.getDni().isBlank()
                || usuario.getNombre_completo() == null || usuario.getNombre_completo().isBlank()
                || usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            return panelConError(model, "Faltan datos obligatorios del usuario.", usuario, null, null, null);
        }

        if (id_usuario == null && (password == null || password.isBlank())) {
            return panelConError(model, "La contraseña es obligatoria para crear usuarios.", usuario, null, null, null);
        }

        Usuario emailExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (emailExistente != null && (usuario.getId_usuario() == null || !emailExistente.getId_usuario().equals(usuario.getId_usuario()))) {
            return panelConError(model, "Ese email ya está registrado.", usuario, null, null, null);
        }

        Usuario dniExistente = usuarioRepository.findByDni(usuario.getDni());
        if (dniExistente != null && (usuario.getId_usuario() == null || !dniExistente.getId_usuario().equals(usuario.getId_usuario()))) {
            return panelConError(model, "Ese DNI ya está registrado.", usuario, null, null, null);
        }

        if (password != null && !password.isBlank()) {
            if (confirmPassword == null || confirmPassword.isBlank() || !password.equals(confirmPassword)) {
                return panelConError(model, "Las contraseñas no coinciden.", usuario, null, null, null);
            }
            usuario.setPassword(passwordEncoder.encode(password));
        } else if (usuario.getId_usuario() == null) {
            return panelConError(model, "La contraseña es obligatoria para crear usuarios.", usuario, null, null, null);
        }

        usuarioRepository.save(usuario);
        return "redirect:/admin/panel";
    }

    @PostMapping("/usuarios/{id}/borrar")
    public String borrarUsuario(@PathVariable Integer id) {
        usuarioRepository.deleteById(id);
        return "redirect:/admin/panel";
    }

    @PostMapping("/cursos/guardar")
    public String guardarCurso(@RequestParam(required = false) Integer id_curso,
                               @RequestParam String nombre,
                               @RequestParam(required = false) String descripcion,
                               Model model) {
        Curso curso = id_curso != null ? cursoRepository.findById(id_curso).orElse(new Curso()) : new Curso();
        curso.setNombre(normalizar(nombre));
        curso.setDescripcion(normalizarTexto(descripcion));
        if (curso.getNombre() == null || curso.getNombre().isBlank()) {
            return panelConError(model, "El curso necesita un nombre.", null, curso, null, null);
        }
        cursoRepository.save(curso);
        return "redirect:/admin/panel";
    }

    @PostMapping("/cursos/{id}/borrar")
    public String borrarCurso(@PathVariable Integer id) {
        cursoRepository.deleteById(id);
        return "redirect:/admin/panel";
    }

    @PostMapping("/ubicaciones/guardar")
    public String guardarUbicacion(@RequestParam(required = false) Integer id_ubicacion,
                                   @RequestParam String nombre,
                                   @RequestParam(required = false) String descripcion,
                                   Model model) {
        Ubicacion ubicacion = id_ubicacion != null ? ubicacionRepository.findById(id_ubicacion).orElse(new Ubicacion()) : new Ubicacion();
        ubicacion.setNombre(normalizar(nombre));
        ubicacion.setDescripcion(normalizarTexto(descripcion));
        if (ubicacion.getNombre() == null || ubicacion.getNombre().isBlank()) {
            return panelConError(model, "La ubicación necesita un nombre.", null, null, ubicacion, null);
        }
        ubicacionRepository.save(ubicacion);
        return "redirect:/admin/panel";
    }

    @PostMapping("/ubicaciones/{id}/borrar")
    public String borrarUbicacion(@PathVariable Integer id) {
        ubicacionRepository.deleteById(id);
        return "redirect:/admin/panel";
    }

    @PostMapping("/inmersiones/guardar")
    public String guardarInmersion(@RequestParam(required = false) Integer id_inmersion,
                                   @RequestParam String nombre,
                                   @RequestParam(required = false) String descripcion,
                                   @RequestParam(required = false) String datos,
                                   @RequestParam String dificultad,
                                   @RequestParam Integer ubicacionId,
                                   Model model) {
        Inmersion inmersion = id_inmersion != null ? inmersionRepository.findById(id_inmersion).orElse(new Inmersion()) : new Inmersion();
        inmersion.setNombre(normalizar(nombre));
        inmersion.setDescripcion(normalizarTexto(descripcion));
        inmersion.setDatos(normalizarTexto(datos));
        try {
            inmersion.setDificultad(Inmersion.Dificultad.valueOf(dificultad));
        } catch (IllegalArgumentException ex) {
            return panelConError(model, "Debes seleccionar una dificultad válida.", null, null, null, inmersion);
        }
        inmersion.setUbicacion(ubicacionRepository.findById(ubicacionId).orElse(null));

        if (inmersion.getNombre() == null || inmersion.getNombre().isBlank()) {
            return panelConError(model, "La inmersión necesita un nombre.", null, null, null, inmersion);
        }
        if (inmersion.getNombre().length() > 150) {
            return panelConError(model, "El nombre de la inmersión no puede superar 150 caracteres.", null, null, null, inmersion);
        }
        if (inmersion.getUbicacion() == null) {
            return panelConError(model, "Debes seleccionar una ubicación válida.", null, null, null, inmersion);
        }
        try {
            inmersionRepository.save(inmersion);
            return "redirect:/admin/panel";
        } catch (DataIntegrityViolationException ex) {
            log.warn("Error de integridad al guardar inmersión {}", inmersion.getNombre(), ex);
            return panelConError(model, "No se pudo guardar la inmersión por una restricción de datos.", null, null, null, inmersion);
        } catch (Exception ex) {
            log.error("Error inesperado al guardar inmersión {}", inmersion.getNombre(), ex);
            return panelConError(model, "Ha ocurrido un error al guardar la inmersión.", null, null, null, inmersion);
        }
    }

    @PostMapping("/inmersiones/{id}/borrar")
    public String borrarInmersion(@PathVariable Integer id) {
        inmersionRepository.deleteById(id);
        return "redirect:/admin/panel";
    }

    @GetMapping("/api/stats")
    @ResponseBody
    public String getStats() {
        long totalUsuarios = usuarioRepository.count();
        long usuariosNoVerificados = usuariosPendientesVerificacion();
        long totalCursos = cursoRepository.count();
        long totalReservas = reservaRepository.count();

        return String.format(
                "{\"totalUsuarios\": %d, \"usuariosNoVerificados\": %d, \"totalCursos\": %d, \"totalReservas\": %d}",
                totalUsuarios, usuariosNoVerificados, totalCursos, totalReservas
        );
    }

    private void cargarDashboard(Model model) {
        model.addAttribute("totalUsuarios", usuarioRepository.count());
        model.addAttribute("totalCursos", cursoRepository.count());
        model.addAttribute("totalUbicaciones", ubicacionRepository.count());
        model.addAttribute("totalInmersiones", inmersionRepository.count());
        model.addAttribute("totalReservas", reservaRepository.count());
        model.addAttribute("totalActividades", actividadRepository.count());
        model.addAttribute("totalInstructores", instructorRepository.count());
        model.addAttribute("totalUsuariosCursos", usuariosCursosRepository.count());
        model.addAttribute("totalInstructoresReservas", instructoresReservasRepository.count());
        model.addAttribute("totalWebFooters", webFooterRepository.count());
        model.addAttribute("usuariosPendientesVerificacion", usuariosPendientesVerificacion());

        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("cursos", cursoRepository.findAll());
        model.addAttribute("ubicaciones", ubicacionRepository.findAll());
        model.addAttribute("inmersiones", inmersionRepository.findAllWithUbicacion());
        model.addAttribute("reservas", reservaRepository.findAll());
        model.addAttribute("usuariosCursos", usuariosCursosRepository.findAll());
        model.addAttribute("actividades", actividadRepository.findAll());
        model.addAttribute("instructores", instructorRepository.findAll());
        model.addAttribute("instructoresReservas", instructoresReservasRepository.findAll());
        model.addAttribute("webFooters", webFooterRepository.findAllByOrderByIdFooterAsc());
        model.addAttribute("ubicacionesParaSelect", ubicacionRepository.findAll());
        model.addAttribute("dificultades", Inmersion.Dificultad.values());
    }

    private String panelConError(Model model, String error, Usuario usuario, Curso curso, Ubicacion ubicacion, Inmersion inmersion) {
        cargarDashboard(model);
        model.addAttribute("error", error);
        model.addAttribute("usuarioFormulario", usuario != null ? usuario : new Usuario());
        model.addAttribute("cursoFormulario", curso != null ? curso : new Curso());
        model.addAttribute("ubicacionFormulario", ubicacion != null ? ubicacion : new Ubicacion());
        model.addAttribute("inmersionFormulario", inmersion != null ? inmersion : new Inmersion());
        return "admin/panel";
    }

    private long usuariosPendientesVerificacion() {
        return usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getTitulaciones() != null && !u.getTitulaciones().isBlank())
                .filter(u -> !u.getVerificar_titulacion())
                .count();
    }

    private String normalizar(String valor) {
        return valor == null ? null : valor.trim();
    }

    private String normalizarTexto(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isBlank() ? null : limpio;
    }

    private String normalizarEmail(String valor) {
        if (valor == null) {
            return null;
        }
        return valor.trim().toLowerCase();
    }

    private Usuario.Rol parseRol(String rol, Usuario.Rol fallback) {
        if (rol == null || rol.isBlank()) {
            return fallback != null ? fallback : Usuario.Rol.USUARIO;
        }
        try {
            return Usuario.Rol.valueOf(rol);
        } catch (IllegalArgumentException ex) {
            return Usuario.Rol.USUARIO;
        }
    }
}
