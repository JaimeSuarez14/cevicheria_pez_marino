package com.proyecto.cevicheria_pez_marino.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/reserva")
public class ReservaController {

    @GetMapping("/degustacion")
    public String reserva(){
        return "ventana_reserva1";
    }

    @GetMapping("/personas")
    public String reserva2(){
        return "ventana_reserva2";
    }

    @GetMapping("/fecha")
    public String reserva3(){
        return "ventana_reserva3";
    }
    
    @GetMapping("/hora")
    public String reserva4(){
        return "ventana_reserva4";
    }

    @GetMapping("/datos")
    public String reserva5(HttpServletRequest request, Model model) {
        model.addAttribute("request", request);
        return "ventana_reserva5";
    }
}
