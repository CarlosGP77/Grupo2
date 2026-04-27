package com.example.demo.service;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo) {
        this.repo = repo;
    }
    // Devuelve la lista completa de usuarios de la BD.
    // findAll() es como un SELECT * FROM usuario.
    public List<Usuario> obtenerTodos() {
        return repo.findAll();
    }
    // Guarda un usuario.
    //   - Si el id es null  → ejecuta un INSERT (usuario nuevo).
    //   - Si el id tiene valor → ejecuta un UPDATE (usuario existente).
    // Esto permite usar el mismo método tanto para crear como para editar.
    public void guerdarUsuario(Usuario usu) {
        repo.save(usu);
    }
    // Elimina el usuario con el id indicado.
    // deleteById() es com un DELETE FROM usuario WHERE id = ?
    public void borrarUsuario(Long id) {
        repo.deleteById(id);
    }
    // Busca un usuario por su id.
    // obtiene el objeto directamente o null si no existe.
    public Usuario obtenerPorId(Long id) {
        return repo.findById(id).orElse(null);
    }
}