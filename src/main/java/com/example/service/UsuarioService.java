package com.example.service;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todos los usuarios
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por ID
    public Optional<Usuario> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    // Obtener un usuario por DNI
    public Usuario obtenerPorDni(String dni) {
        return usuarioRepository.findByDni(dni);
    }

    // Obtener un usuario por email
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Guardar o actualizar un usuario
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Eliminar un usuario por ID
    public void eliminarPorId(Integer id) {
        usuarioRepository.deleteById(id);
    }

    // Eliminar un usuario por DNI
    public void eliminarPorDni(String dni) {
        Usuario u = usuarioRepository.findByDni(dni);
        if (u != null) {
            usuarioRepository.delete(u);
        }
    }

    // Registrar un nuevo usuario
    public Usuario registrar(Usuario usuario) {
        // Aquí puedes agregar validaciones y lógica de negocio
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            throw new RuntimeException("El email ya está registrado");
        }
        return usuarioRepository.save(usuario);
    }

    // Verificar si existe un usuario por ID
    public boolean existePorId(Integer id) {
        return usuarioRepository.existsById(id);
    }

    // Verificar si existe un usuario por DNI
    public boolean existePorDni(String dni) {
        return usuarioRepository.findByDni(dni) != null;
    }
}
