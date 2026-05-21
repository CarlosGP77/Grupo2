package com.example.controller;

import com.example.model.Actividad;
import com.example.service.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @GetMapping("/actividades")
    public String listar(Model model){
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
}

