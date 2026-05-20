package com.example.controller;

import com.example.service.UbicacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UbicacionController {

    private final UbicacionService servicio;

    public UbicacionController(UbicacionService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/ubicaciones")
    public String listarUbicaciones(Model model) {
        model.addAttribute("listaUbicaciones", servicio.listarTodas());
        return "html/ubicaciones";
    }
}
