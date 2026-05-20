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
    public List<Inmersion> listarTodas() {
        return repo.findAllWithUbicacion();
    }
    public Inmersion buscarPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }
    public Inmersion guardar(Inmersion inmersion) {
        return repo.save(inmersion);
    }
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
}
