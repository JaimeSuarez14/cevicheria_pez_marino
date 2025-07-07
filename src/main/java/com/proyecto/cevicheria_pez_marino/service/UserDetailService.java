package com.proyecto.cevicheria_pez_marino.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.proyecto.cevicheria_pez_marino.model.Usuario;
import com.proyecto.cevicheria_pez_marino.repository.UsuarioRepository;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);
        if (usuarioOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        Usuario user = usuarioOptional.get();
        // Ahora, en lugar de construir un org.springframework.security.core.userdetails.User,
        // devolvemos directamente tu objeto Usuario, ya que ahora implementa UserDetails.
        return user; 
    
    }
    

}


