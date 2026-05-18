package com.example.controller;

import com.example.service.InmersionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InmersionController {

    private final InmersionService servicio;

    public InmersionController(InmersionService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/inmersiones")
    public String listarInmersiones(Model model) {
        model.addAttribute("listaInmersiones", servicio.listarTodas());
        return "inmersiones"; // inmersiones.html
    }
}

