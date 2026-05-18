package com.example.service;
import com.example.model.Inmersion;
import com.example.repository.InmersionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InmersionService {

    private final InmersionRepository repo;

    public InmersionService(InmersionRepository repo) {
        this.repo = repo;
    }

    // Obtener todas las inmersiones
    public List<Inmersion> listarTodas() {
        return repo.findAll();
    }

    // Buscar una inmersión por ID
    public Inmersion buscarPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    // Guardar o actualizar una inmersión
    public Inmersion guardar(Inmersion inmersion) {
        return repo.save(inmersion);
    }

    // Eliminar una inmersión
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
}

