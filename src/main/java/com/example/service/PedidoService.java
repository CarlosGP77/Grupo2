package com.example.service;
import com.example.model.Usuario;
import com.example.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository repo;
    public PedidoService(PedidoRepository repo) {this.repo = repo;}
    public List<Usuario> obtenerTodoPedido() {
        return repo.findAll();
    }
    public void guerdarPedido(Usuario usu) {
        repo.save(usu);
    }
    public void borrarPedido(Long id) {
        repo.deleteById(id);
    }
    public Usuario obtenerPorIdPedido(Long id) {
        return repo.findById(id).orElse(null);
    }
}
