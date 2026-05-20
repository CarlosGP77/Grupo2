package com.example.service;

import com.example.model.Ubicacion;
import com.example.repository.UbicacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UbicacionService {

    private final UbicacionRepository repo;

    public UbicacionService(UbicacionRepository repo) {
        this.repo = repo;
    }
    public List<Ubicacion> listarTodas() {
        return repo.findAll();
    }
    public Ubicacion buscarPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }
    public Ubicacion guardar(Ubicacion ubicacion) {
        return repo.save(ubicacion);
    }
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
    public Ubicacion buscarPorNombre(String nombre) {
        return repo.findByNombre(nombre);
    }
    public List<Ubicacion> buscarPorNombreParcial(String nombre) {
        return repo.findByNombreContainingIgnoreCase(nombre);
    }
}

