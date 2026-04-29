package com.example.tienda.controller;
import com.example.tienda.model.Pedido;
import com.example.tienda.model.Producto;
import com.example.tienda.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PedidoController {
    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/listapedidos")
    public String listar(Model model) {
        model.addAttribute("todos", service.obtenerTodoPedido());
        return "pedidos/lista";
    }
    @GetMapping("/nuevopedidos")
    public String form(Model model) {
        model.addAttribute("usaux", new Producto());
        return "pedidos/form";
    }
    @GetMapping("/editarpedidos/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("usaux", service.obtenerPorIdPedido(id));
        return "pedidos/form";
    }
    @GetMapping("/borrarpedidos/{id}")
    public String borrar(@PathVariable Long id) {
        service.borrarPedido(id);
        return "redirect:/pedidos/listapedidos";
    }
    @PostMapping("/guardarpedidos")
    public String guardar(Pedido u) {
        service.guerdarPedido(u);
        return "redirect:/pedidos/listapedidos";
    }
}
