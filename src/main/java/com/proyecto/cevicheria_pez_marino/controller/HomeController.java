package com.proyecto.cevicheria_pez_marino.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/principal")
public class HomeController {

    @GetMapping("/")
    public String principal(){
        return "principal";
    }

    @GetMapping("/menu")
    public String menuComidas(){
        return "menu_comidas";
    }

    @GetMapping("/pedidos")
    public String pedidos(){
        return "Pedidos";
    }
}
