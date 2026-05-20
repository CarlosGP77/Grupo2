package com.example.service;

import com.example.model.Curso;
import com.example.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;
    public List<Curso> obtenerTodos() {
        return cursoRepository.findAll();
    }
    public Optional<Curso> obtenerPorId(Integer id) {
        return cursoRepository.findById(id);
    }
    public List<Curso> buscarPorNombre(String nombre) {
        return cursoRepository.findByNombre(nombre);
    }
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }
    public void eliminar(Integer id) {
        cursoRepository.deleteById(id);
    }
    public Curso crear(Curso curso) {
        return cursoRepository.save(curso);
    }
    public Curso actualizar(Integer id, Curso cursoActualizado) {
        Optional<Curso> curso = cursoRepository.findById(id);
        if (curso.isPresent()) {
            Curso c = curso.get();
            c.setNombre(cursoActualizado.getNombre());
            c.setDescripcion(cursoActualizado.getDescripcion());
            return cursoRepository.save(c);
        }
        return null;
    }
    public long contar() {
        return cursoRepository.count();
    }
}

