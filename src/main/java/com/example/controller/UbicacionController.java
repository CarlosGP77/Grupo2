package com.example.controller;

import com.example.model.Ubicacion;
import com.example.service.UbicacionService;
import com.example.service.WebFooterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UbicacionController {

    private final UbicacionService servicio;
    private final WebFooterService footerService;

    public UbicacionController(UbicacionService servicio, WebFooterService footerService) {
        this.servicio = servicio;
        this.footerService = footerService;
    }

    private void addFooterAttributes(Model model) {
        model.addAttribute("footerEmpresa", footerService.obtenerTextoPorTipo("empresa"));
        model.addAttribute("footerDescripcion", footerService.obtenerTextoPorTipo("descripcion"));
        model.addAttribute("footerDireccion", footerService.obtenerTextoPorTipo("direccion"));
        model.addAttribute("footerCopyright", footerService.obtenerTextoPorTipo("copyright"));
        model.addAttribute("footerEnlacesLegales", footerService.obtenerContenidosPorTipo("enlace_legal"));
        model.addAttribute("footerEmpresaInfo", footerService.obtenerContenidosPorTipo("empresa_info"));
        model.addAttribute("footerRedesSociales", footerService.obtenerContenidosPorTipo("red_social"));
    }

    @GetMapping("/ubicaciones")
    public String listarUbicaciones(Model model) {
        model.addAttribute("listaUbicaciones", servicio.listarTodas());
        addFooterAttributes(model);
        return "html/ubicaciones";
    }

    @GetMapping("/ubicaciones/{id}")
    public String verUbicacion(@PathVariable Integer id, Model model) {
        Ubicacion ubicacion = servicio.buscarPorId(id);
        if (ubicacion != null) {
            model.addAttribute("ubicacion", ubicacion);
            addFooterAttributes(model);
            return "html/ubicacion-detalle";
        }

        model.addAttribute("status", 404);
        model.addAttribute("message", "No se encontró la ubicación solicitada.");
        model.addAttribute("error", "Ubicación no encontrada");
        addFooterAttributes(model);
        return "html/error";
    }
}
