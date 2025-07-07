package com.proyecto.cevicheria_pez_marino.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.cevicheria_pez_marino.dto.ListaCarrito;
import com.proyecto.cevicheria_pez_marino.model.Producto;
import com.proyecto.cevicheria_pez_marino.service.ProductoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/principal")
@RequiredArgsConstructor
public class HomeController {

    private final ProductoService productoService;

    @GetMapping
    public String principal(HttpServletRequest request, Model model) {
        model.addAttribute("request", request);
        return "principal";
    }

    @GetMapping("/menu")
    public String menuComidas(Model model, HttpSession session){

        @SuppressWarnings("unchecked")
        List<ListaCarrito> listaDeLaSession = (List<ListaCarrito>) session.getAttribute("listaCarrito");

        if (listaDeLaSession == null || listaDeLaSession.isEmpty()) {
            listaDeLaSession = new ArrayList<>(); // Si no hay carrito en sesión, inicializa una lista vacía para
        }

        /*if (model.containsAttribute("mensajeNotificacion") && listaDeLaSession.isEmpty()) {
            model.addAttribute("mensajeNotificaciones", "Carrito vacío, por favor agregue productos.");
            model.addAttribute("tipoNotificacion", "danger");
        }*/
        
        session.setAttribute("listaCarrito", listaDeLaSession);
        model.addAttribute("productos", productoService.findAll());
        return "menu_comidas";
    }

    @GetMapping("/pedidos")
    public String pedidos(){
        return "Pedidos";
    }

    @PostMapping("/buscar")
    public String buscarProducto(@RequestParam("busqueda") String busqueda, Model model, HttpSession session) {

        List<Producto> productos = productoService.buscarPorNombre(busqueda);
        model.addAttribute("productos", productos);

        session.setAttribute("listaCarrito", session.getAttribute("listaCarrito"));

        //obtener el usuario
        return "menu_comidas";
        
    }
    
    @GetMapping("/test-protected")
    public String testProtected() {
        return "test_protected";
    }

}
