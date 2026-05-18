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

    // Listar todas las ubicaciones
    public List<Ubicacion> listarTodas() {
        return repo.findAll();
    }

    // Buscar por ID
    public Ubicacion buscarPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    // Guardar o actualizar
    public Ubicacion guardar(Ubicacion ubicacion) {
        return repo.save(ubicacion);
    }

    // Eliminar
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }

    // Buscar por nombre exacto
    public Ubicacion buscarPorNombre(String nombre) {
        return repo.findByNombre(nombre);
    }

    // Buscar por coincidencia parcial
    public List<Ubicacion> buscarPorNombreParcial(String nombre) {
        return repo.findByNombreContainingIgnoreCase(nombre);
    }
}

