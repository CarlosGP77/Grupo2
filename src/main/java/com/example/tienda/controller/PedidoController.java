package com.example.tienda.controller;
import com.example.tienda.model.Pedido;
import com.example.tienda.model.Producto;
import com.example.tienda.service.PedidoService;
import com.example.tienda.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService service;
    private final ProductoService productoService;

    public PedidoController(PedidoService service, ProductoService productoService) {
        this.service = service;
        this.productoService = productoService;
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
        model.addAttribute("todosProductos", productoService.obtenerTodoProducto());
        return "pedidos/form";
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", service.obtenerPorIdPedido(id));
        model.addAttribute("todosProductos", productoService.obtenerTodoProducto());
        return "pedidos/form";
    }
    @GetMapping("/borrar/{id}")
    public String borrar(@PathVariable Long id) {
        service.borrarPedido(id);
        return "redirect:/pedidos/lista";
    }
    @PostMapping("/guardar")
    public String guardar(Pedido u, @RequestParam(value = "productosSeleccionados", required = false) List<Long> productosIds) {
        // Limpiar productos previos si es edición
        if (u.getId() != null) {
            u.getProductos().clear();
        } else {
            u.setProductos(new ArrayList<>());
        }

        // Agregar los productos seleccionados
        if (productosIds != null && !productosIds.isEmpty()) {
            for (Long productoId : productosIds) {
                Producto producto = productoService.obtenerPorIdProducto(productoId);
                if (producto != null) {
                    u.addProducto(producto);
                }
            }
        }

        service.guerdarPedido(u);
        return "redirect:/pedidos/lista";
    }
}
