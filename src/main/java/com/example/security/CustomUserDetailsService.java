package com.example.security;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByDni(username);
        if (u == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        String password = u.getPassword() == null ? "" : u.getPassword();

        // Asignar rol basado en el campo rol de Usuario
        String role = u.getRol() != null ? u.getRol().name() : "USUARIO";

        return User.withUsername(u.getDni())
                .password(password)
                .roles(role)  // ADMIN, VERIFICADOR, o USUARIO
                .build();
    }
}

