package com.example.security;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByEmail(email);
        if (u == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }
        String password = u.getPassword() == null ? "" : u.getPassword();

        String role = u.getRol() != null ? u.getRol().name() : "USUARIO";

        return User.withUsername(u.getEmail())
                .password(password)
                .roles(role)  // ADMIN, VERIFICADOR, o USUARIO
                .build();
    }
}

