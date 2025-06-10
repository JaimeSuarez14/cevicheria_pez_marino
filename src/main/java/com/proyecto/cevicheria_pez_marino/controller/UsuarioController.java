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

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/login")
    public String login(){
        return "usuario/login";
    }

    @GetMapping("/registro")
    public String registro(){
        return "usuario/registro";
    }

    @PostMapping("/registrarUsuario")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model){

    //vamos validad que los campos no esten vacios
    if(usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()){
        model.addAttribute("error1", "El nombre es obligatorio");
        return "usuario/registro";
    }
    Optional<Usuario> usuarioOptional = usuarioService.findByEmail(usuario.getEmail());
    if(usuarioOptional.isPresent()){
        model.addAttribute("error", "El email ya esta registrado");
        return "usuario/registro";
        
    }
        usuarioService.save(usuario);
        return "usuario/login";
    }
}
