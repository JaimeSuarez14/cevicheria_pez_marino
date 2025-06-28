package com.proyecto.cevicheria_pez_marino.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.proyecto.cevicheria_pez_marino.model.Usuario;
import com.proyecto.cevicheria_pez_marino.service.UsuarioService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import io.micrometer.common.util.StringUtils;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "usuario/login";
    }

    @GetMapping("/registro")
    public String registro() {
        return "usuario/registro";
    }

    @PostMapping("/registrarUsuario")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {

        // Validamos que los campos no estén vacíos con espacios
        if (StringUtils.isBlank(usuario.getNombre()) ||
                StringUtils.isBlank(usuario.getUsername()) ||
                StringUtils.isBlank(usuario.getDireccion()) ||
                StringUtils.isBlank(usuario.getCelular()) ||
                StringUtils.isBlank(usuario.getPassword())) {
            model.addAttribute("error", "Todos los campos son obligatorios");
            return "usuario/registro";
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            model.addAttribute("error", "El email es obligatorio");
            return "usuario/registro";
        }

        // Validación básica de formato de email
        if (!usuario.getEmail().contains("@") || !usuario.getEmail().contains(".")) {
            model.addAttribute("error", "El formato del email no es válido");
            return "usuario/registro";
        }

        Optional<Usuario> usuarioOptional = usuarioService.findByEmail(usuario.getEmail());

        if (usuarioOptional.isPresent()) {
            model.addAttribute("error", "El email ya está registrado");
            return "usuario/registro";
        }

        Optional<Usuario> usuarioUsername = usuarioService.buscarPorUsername(usuario.getUsername());
        if (usuarioUsername.isPresent()) {
            model.addAttribute("error", "El nombre de usuario ya está registrado");
            return "usuario/registro";
        }

        usuarioService.save(usuario);
        model.addAttribute("mensaje", "Usuario registrado exitosamente. Por favor inicia sesión.");
        return "usuario/login";
    }
}
