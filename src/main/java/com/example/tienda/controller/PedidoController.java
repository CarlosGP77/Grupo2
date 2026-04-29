package com.example.tienda.controller;
import com.example.tienda.model.Pedido;
import com.example.tienda.model.Producto;
import com.example.tienda.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("pedidos", service.obtenerTodoPedido());
        return "pedidos/lista";
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("pedidos", service.obtenerTodoPedido());
        return "pedidos/lista";
    }
    @GetMapping("/nuevo")
    public String form(Model model) {
        model.addAttribute("pedido", new Pedido());
        return "pedidos/form";
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", service.obtenerPorIdPedido(id));
        return "pedidos/form";
    }
    @GetMapping("/borrar/{id}")
    public String borrar(@PathVariable Long id) {
        service.borrarPedido(id);
        return "redirect:/pedidos/lista";
    }
    @PostMapping("/guardar")
    public String guardar(Pedido u) {
        service.guerdarPedido(u);
        return "redirect:/pedidos/lista";
    }
}
