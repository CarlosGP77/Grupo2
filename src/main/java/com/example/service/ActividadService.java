package com.example.service;

import com.example.model.Actividad;
import com.example.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

    // Obtener todas las actividades
    public List<Actividad> obtenerTodos() {
        return actividadRepository.findAll();
    }

    // Obtener un curso por ID
    public Optional<Actividad> obtenerPorId(Integer id) {
        return actividadRepository.findById(id);
    }

    // Buscar actividades por nombre
    public List<Actividad> buscarPorNombre(String nombre) {
        return actividadRepository.findByNombre(nombre);
    }

    // Guardar o actualizar una actividad
    public Actividad guardar(Actividad actividad) {
        return actividadRepository.save(actividad);
    }

    // Eliminar una actividad por ID
    public void eliminar(Integer id) {
        actividadRepository.deleteById(id);
    }

    // Crear una nueva actividad
    public Actividad crear(Actividad actividad) {
        return actividadRepository.save(actividad);
    }

    // Actualizar una actividad existente
    public Actividad actualizar(Integer id, Actividad actividadActualizada) {
        Optional<Actividad> actividad = actividadRepository.findById(id);
        if (actividad.isPresent()) {
            Actividad a = actividad.get();
            a.setNombre(actividadActualizada.getNombre());
            a.setDescripcion(actividadActualizada.getDescripcion());
            a.setTipo(actividadActualizada.getTipo());
            a.setPrecio(actividadActualizada.getPrecio());
            return actividadRepository.save(a);
        }
        return null;
    }

    // Contar total de actividades
    public long contar() {
        return actividadRepository.count();
    }
}
