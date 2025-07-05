package com.proyecto.cevicheria_pez_marino.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.cevicheria_pez_marino.model.Producto;
import com.proyecto.cevicheria_pez_marino.service.ProductoService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductoService productoService;

    @GetMapping("/")
    public String home(){
        return "principal";
    }

    @GetMapping("/principal")
    public String principal(){
        return "principal";
    }

    @GetMapping("/principal/menu")
    public String menuComidas(Model model, HttpSession session){
        session.setAttribute("listaCarrito", session.getAttribute("listaCarrito"));
        model.addAttribute("productos", productoService.findAll());
        return "menu_comidas";
    }

    @GetMapping("/principal/pedidos")
    public String pedidos(){
        return "Pedidos";
    }

    @PostMapping("/principal/buscar")
    public String buscarProducto(@RequestParam("busqueda") String busqueda, Model model, HttpSession session) {

        List<Producto> productos = productoService.buscarPorNombre(busqueda);
        model.addAttribute("productos", productos);

        session.setAttribute("listaCarrito", session.getAttribute("listaCarrito"));

        //obtener el usuario
        return "menu_comidas";
        
    }

}
