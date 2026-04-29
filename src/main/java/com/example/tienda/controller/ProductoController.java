package com.example.tienda.controller;
import com.example.tienda.model.Producto;
import com.example.tienda.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("productos", service.obtenerTodoProducto());
        return "productos/lista";
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("productos", service.obtenerTodoProducto());
        return "productos/lista";
    }
    @GetMapping("/nuevo")
    public String form(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/form";
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("producto", service.obtenerPorIdProducto(id));
        return "productos/form";
    }
    @GetMapping("/borrar/{id}")
    public String borrar(@PathVariable Long id) {
        service.borrarProducto(id);
        return "redirect:/productos/lista";
    }
    @PostMapping("/guardar")
    public String guardar(Producto u) {
        service.guardarProducto(u);
        return "redirect:/productos/lista";
    }
}
