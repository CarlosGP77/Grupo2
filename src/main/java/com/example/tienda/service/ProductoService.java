package com.example.tienda.service;
import com.example.tienda.model.Producto;
import com.example.tienda.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository repo;
    public ProductoService(ProductoRepository repo) {this.repo = repo;}
    public List<Producto> obtenerTodoProducto() {return repo.findAll();}
    public void guardarProducto(Producto usu) {
        repo.save(usu);
    }
    public void borrarProducto(Long id) {
        repo.deleteById(id);
    }
    public Producto obtenerPorIdProducto(Long id) {
        return repo.findById(id).orElse(null);
    }
}