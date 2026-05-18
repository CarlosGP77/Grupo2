package com.example.controller;
import com.example.service.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @GetMapping("/actividades")
    public String listar(Model model){
        model.addAttribute("actividades", actividadService.obtenerTodos());
        return "actividades";
    }

}
