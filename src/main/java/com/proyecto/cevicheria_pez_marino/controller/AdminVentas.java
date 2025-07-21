package com.proyecto.cevicheria_pez_marino.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.proyecto.cevicheria_pez_marino.model.Ordenes;
import com.proyecto.cevicheria_pez_marino.service.OrdenesService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/adminVenta")
@RequiredArgsConstructor
public class AdminVentas {

    private final OrdenesService ordenesService;

    @GetMapping("/listaOrdenes")
    public String listarOrdenes(Model model) {

        List<Ordenes> ordenes = ordenesService.findAll();

        model.addAttribute("ordenes", ordenes);
        model.addAttribute("filtro", "todos");
        return "administracion/listaOrdenes";
    }
}
