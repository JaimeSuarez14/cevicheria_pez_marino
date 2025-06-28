package com.proyecto.cevicheria_pez_marino.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto.cevicheria_pez_marino.model.Usuario;
import com.proyecto.cevicheria_pez_marino.repository.UsuarioRepository;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario save(Usuario usuario) {

        String clave = usuario.getPassword();
        if (clave != null && !clave.isEmpty()) {
            // Aquí podrías agregar lógica para encriptar la contraseña si es necesario
         
            usuario.setPassword(passwordEncoder.encode(clave));
        }

        // Si no se especifica rol, asignar "USER" por defecto
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("USER");
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario saveAdmin(Usuario usuario) {
        String clave = usuario.getPassword();
        if (clave != null && !clave.isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(clave));
        }
        usuario.setRol("ADMIN");
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // Método para crear un administrador por defecto si no existe ninguno
    public void crearAdminPorDefecto() {
        Optional<Usuario> adminExistente = usuarioRepository.findByUsername("admin");
        if (adminExistente.isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setUsername("admin");
            admin.setEmail("admin@elpezmarino.com");
            admin.setPassword("admin123");
            admin.setCelular("999999999");
            admin.setDireccion("Lima, Perú");
            admin.setRol("ADMIN");
            
            saveAdmin(admin);
        }
    }
}
