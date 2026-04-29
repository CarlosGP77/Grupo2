package com.example.tienda.controller;
import com.example.tienda.model.Producto;
import com.example.tienda.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductoController {
    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/listaproductos")
    public String listar(Model model) {
        model.addAttribute("todos", service.obtenerTodoProducto());
        return "productos/lista";
    }
    @GetMapping("/nuevoproducto")
    public String form(Model model) {
        model.addAttribute("usaux", new Producto());
        return "productos/form";
    }
    @GetMapping("/editarproducto/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("usaux", service.obtenerPorIdProducto(id));
        return "productos/form";
    }
    @GetMapping("/borrarproducto/{id}")
    public String borrar(@PathVariable Long id) {
        service.borrarProducto(id);
        return "redirect:/productos/listaproductos";
    }
    @PostMapping("/guardarproducto")
    public String guardar(Producto u) {
        service.guardarProducto(u);
        return "redirect:/productos/listaproductos";
    }
}
