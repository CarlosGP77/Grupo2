package com.example.service;
import com.example.model.Reservas;
import com.example.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository repo;
    public ProductoService(ProductoRepository repo) {this.repo = repo;}
    public List<Reservas> obtenerTodoProducto() {return repo.findAll();}
    public void guardarProducto(Reservas usu) {
        repo.save(usu);
    }
    public void borrarProducto(Long id) {
        repo.deleteById(id);
    }
    public Reservas obtenerPorIdProducto(Long id) {
        return repo.findById(id).orElse(null);
    }
}