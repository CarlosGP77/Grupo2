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

    // Obtener todos los cursos
    public List<Curso> obtenerTodos() {
        return cursoRepository.findAll();
    }

    // Obtener un curso por ID
    public Optional<Curso> obtenerPorId(Integer id) {
        return cursoRepository.findById(id);
    }

    // Buscar cursos por nombre
    public List<Curso> buscarPorNombre(String nombre) {
        return cursoRepository.findByNombre(nombre);
    }

    // Guardar o actualizar un curso
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    // Eliminar un curso por ID
    public void eliminar(Integer id) {
        cursoRepository.deleteById(id);
    }

    // Crear un nuevo curso
    public Curso crear(Curso curso) {
        return cursoRepository.save(curso);
    }

    // Actualizar un curso existente
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

    // Contar total de cursos
    public long contar() {
        return cursoRepository.count();
    }
}

