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
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }
    public Optional<Usuario> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id);
    }
    public Usuario obtenerPorDni(String dni) {
        return usuarioRepository.findByDni(dni);
    }
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    public void eliminarPorId(Integer id) {
        usuarioRepository.deleteById(id);
    }
    public void eliminarPorDni(String dni) {
        Usuario u = usuarioRepository.findByDni(dni);
        if (u != null) {
            usuarioRepository.delete(u);
        }
    }
    public Usuario registrar(Usuario usuario) {
        // Aquí puedes agregar validaciones y lógica de negocio
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            throw new RuntimeException("El email ya está registrado");
        }
        return usuarioRepository.save(usuario);
    }
    public boolean existePorId(Integer id) {
        return usuarioRepository.existsById(id);
    }
    public boolean existePorDni(String dni) {
        return usuarioRepository.findByDni(dni) != null;
    }
}
