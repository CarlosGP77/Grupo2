package com.example.tienda.service;
import com.example.tienda.model.Pedido;
import com.example.tienda.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository repo;
    public PedidoService(PedidoRepository repo) {this.repo = repo;}
    public List<Pedido> obtenerTodoPedido() {
        return repo.findAll();
    }
    public void guerdarPedido(Pedido usu) {
        repo.save(usu);
    }
    public void borrarPedido(Long id) {
        repo.deleteById(id);
    }
    public Pedido obtenerPorIdPedido(Long id) {
        return repo.findById(id).orElse(null);
    }
}
