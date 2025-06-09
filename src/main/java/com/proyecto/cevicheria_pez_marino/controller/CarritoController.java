package com.proyecto.cevicheria_pez_marino.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @GetMapping("/productos")
    public String verVentanaProductos() {
        return "Carrito/productos";
    }
    
    @GetMapping("/cliente")
    public String verVentanaCliente() {
        return "Carrito/cliente";
    }

    @GetMapping("/envio")
    public String verVentanaEnvio() {
        return "Carrito/envio";
    }

    @GetMapping("/pago")
    public String verVentanaPago() {
        return "Carrito/pago";
    }

    @GetMapping("/finalizar")
    public String verVentanaFinalizar() {
        return "Carrito/finalizar";
    }

    @GetMapping("/resumenCompra")
    public String verVentanaResumenCompra() {
        return "Carrito/resumenCompra";
    }
}
