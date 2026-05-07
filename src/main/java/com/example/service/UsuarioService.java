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

    // Obtener un usuario por DNI
    public Optional<Usuario> obtenerPorDni(String dni) {
        return usuarioRepository.findById(dni);
    }

    // Obtener un usuario por email
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Guardar o actualizar un usuario
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Eliminar un usuario por DNI
    public void eliminar(String dni) {
        usuarioRepository.deleteById(dni);
    }

    // Registrar un nuevo usuario
    public Usuario registrar(Usuario usuario) {
        // Aquí puedes agregar validaciones y lógica de negocio
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            throw new RuntimeException("El email ya está registrado");
        }
        return usuarioRepository.save(usuario);
    }

    // Verificar si existe un usuario
    public boolean existe(String dni) {
        return usuarioRepository.existsById(dni);
    }
}

