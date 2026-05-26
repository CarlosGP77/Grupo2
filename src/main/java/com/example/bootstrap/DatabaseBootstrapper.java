package com.example.bootstrap;

import com.example.model.Actividad;
import com.example.model.Ubicacion;
import com.example.model.Usuario;
import com.example.model.WebFooter;
import com.example.repository.ActividadRepository;
import com.example.repository.UbicacionRepository;
import com.example.repository.UsuarioRepository;
import com.example.repository.WebFooterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

import org.jspecify.annotations.NonNull;

@Component
public classDatabaseBootstrapper implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseBootstrapper.class);

    private final UsuarioRepository usuarioRepository;
    private final ActividadRepository actividadRepository;
    private final UbicacionRepository ubicacionRepository;
    private final WebFooterRepository webFooterRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseBootstrapper(UsuarioRepository usuarioRepository,
                                ActividadRepository actividadRepository,
                                UbicacionRepository ubicacionRepository,
                                WebFooterRepository webFooterRepository,
                                PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.actividadRepository = actividadRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.webFooterRepository = webFooterRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(@NonNull ApplicationArguments args) {
        List.of(
                new UserSeed("12345678A", "Administrador del Sistema", "admin@example.com", "Admin_123", Usuario.Rol.ADMIN, true),
                new UserSeed("87654321B", "Verificador de Credenciales", "verificador@example.com", "Admin_123", Usuario.Rol.VERIFICADOR, true)
        ).forEach(this::ensureUser);

        ensureActividadData();
        ensureUbicacionData();
        ensureFooterData();
    }

    private void ensureUser(UserSeed seed) {
        String dni = seed.dni();
        String nombreCompleto = seed.nombreCompleto();
        String email = seed.email();
        String rawPassword = seed.rawPassword();
        Usuario.Rol rol = seed.rol();
        boolean verificado = seed.verificado();

p        Usuario usuario = usuarioRepository.findByDni(dni);
        if (usuario == null) {
            usuario = usuarioRepository.findByEmail(email);
        }

        if (usuario == null) {
            usuario = new Usuario();
            usuario.setDni(dni);
            log.info("Usuario semilla {} no existía; se creará.", email);
        } else {
            log.info("Usuario semilla {} ya existía; se actualizará.", email);
        }

        usuario.setNombre_completo(nombreCompleto);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(rawPassword));
        usuario.setRol(rol);
        usuario.setVerificar_titulacion(verificado);
        usuarioRepository.save(usuario);

        log.info("Usuario {} ({}) asegurado automáticamente.", nombreCompleto, email);
    }

    private void ensureFooterData() {
        if (webFooterRepository.count() > 0) {
            log.info("Datos del footer ya existentes; se omite la creación automática.");
            return;
        }

        webFooterRepository.saveAll(java.util.List.of(
                footer("empresa", "Mourosub"),
                footer("descripcion", "Abyssal Elegance in Diving."),
                footer("direccion", "Puerto Deportivo Marina del Cantábrico"),
                footer("copyright", "©2024 Mourosub."),
                footer("enlace_legal", "Privacy Policy"),
                footer("enlace_legal", "Terms of Service"),
                footer("empresa_info", "Safety Protocols"),
                footer("empresa_info", "Careers"),
                footer("empresa_info", "Press Kit"),
                footer("red_social", "https://www.facebook.com/mourosub?fref=ts"),
                footer("red_social", "https://www.instagram.com/mourosub/"),
                footer("red_social", "https://x.com/mourosub")
        ));

        log.info("Datos del footer creados automáticamente.");
    }

    private void ensureActividadData() {
        if (actividadRepository.count() > 0) {
            log.info("Actividades ya existentes; se omite la creación automática.");
            return;
        }

        Actividad bautismo = new Actividad();
        bautismo.setNombre("Bautismo de buceo");
        bautismo.setDescripcion("Experiencia inicial para probar el buceo.");
        bautismo.setPrecio(new java.math.BigDecimal("90.00"));

        Actividad inmersionGuiada = new Actividad();
        inmersionGuiada.setNombre("Inmersión guiada");
        inmersionGuiada.setDescripcion("Salida guiada para buceadores con experiencia.");
        inmersionGuiada.setPrecio(new java.math.BigDecimal("120.00"));

        actividadRepository.saveAll(java.util.List.of(bautismo, inmersionGuiada));
        log.info("Actividades de ejemplo creadas automáticamente.");
    }

    private void ensureUbicacionData() {
        if (ubicacionRepository.count() > 0) {
            log.info("Ubicaciones ya existentes; se omite la creación automática.");
            return;
        }

        Ubicacion puerto = new Ubicacion();
        puerto.setNombre("Puerto Deportivo Marina del Cantábrico");
        puerto.setDescripcion("Punto de salida principal para las inmersiones.");

        Ubicacion costa = new Ubicacion();
        costa.setNombre("Costa Cantábrica");
        costa.setDescripcion("Zona de inmersión para salidas guiadas.");

        ubicacionRepository.saveAll(java.util.List.of(puerto, costa));
        log.info("Ubicaciones de ejemplo creadas automáticamente.");
    }

    private WebFooter footer(String tipoInfo, String contenido) {
        WebFooter webFooter = new WebFooter();
        webFooter.setTipoInfo(tipoInfo);
        webFooter.setContenido(contenido);
        return webFooter;
    }

    private record UserSeed(String dni,
                            String nombreCompleto,
                            String email,
                            String rawPassword,
                            Usuario.Rol rol,
                            boolean verificado) {
    }
}

