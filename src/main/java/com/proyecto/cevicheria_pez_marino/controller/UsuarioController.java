package com.proyecto.cevicheria_pez_marino.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    
    @GetMapping("/login")
    public String login(){
        return "usuario/login";
    }

    @GetMapping("/registro")
    public String registro(){
        return "usuario/registro";
    }
}
