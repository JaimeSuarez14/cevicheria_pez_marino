package com.proyecto.cevicheria_pez_marino.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.cevicheria_pez_marino.model.Reserva;
import com.proyecto.cevicheria_pez_marino.service.ReservaService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/reserva")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @GetMapping("/degustacion")
    public String reserva(Model model, HttpServletRequest session){
        if (session.getAttribute("currentReserva") == null) {
            session.setAttribute("currentReserva", new Reserva());
        }
        return "reserva/ventana_reserva1";
    }

    @PostMapping("/guardarExperiencia")
    public String guardarExperiencia(@RequestParam("experiencia") String experiencia, HttpSession session) {
        Reserva reserva = (Reserva) session.getAttribute("currentReserva");
        if (reserva == null) {
            reserva = new Reserva();
        }
        reserva.setExperiencia(experiencia);
        session.setAttribute("currentReserva", reserva);
        return "redirect:/reserva/personas"; // Redirige a la siguiente etapa
    }


    @GetMapping("/personas")
    public String reserva2(Model model, HttpSession session){
        Reserva reserva = (Reserva) session.getAttribute("currentReserva");
        if (reserva != null && reserva.getNumPersonas() > 2) {
            model.addAttribute("numPersonas", reserva.getNumPersonas());
        } else {
            reserva = new Reserva();
            reserva.setNumPersonas(2);
            session.setAttribute("currentReserva", reserva);
            model.addAttribute("numPersonas", 2); // Valor por defecto
        }
        return "reserva/ventana_reserva2";
    }


    @PostMapping("/guardarPersonas")
    public String guardarPersonas(@RequestParam("numPersonas") int numPersonas, HttpSession session) {
        Reserva reserva = (Reserva) session.getAttribute("currentReserva");
        if (reserva == null) {
            // Esto no debería pasar si siempre empezamos por /degustacion
            reserva = new Reserva();
        }
        reserva.setNumPersonas(numPersonas);
        session.setAttribute("currentReserva", reserva);
        return "redirect:/reserva/fecha";
    }

    @GetMapping("/fecha")
    public String reserva3(Model model, HttpSession session){
        Reserva reserva = (Reserva) session.getAttribute("currentReserva");
        if (reserva != null && reserva.getFecha() != null) {
            model.addAttribute("fechaSeleccionada", reserva.getFecha());
        }
        return "reserva/ventana_reserva3";
    }

    // Guardar fecha y redirigir a hora
    @PostMapping("/guardarFecha")
    public String guardarFecha(@RequestParam("fecha") String fecha, HttpSession session) {
        Reserva reserva = (Reserva) session.getAttribute("currentReserva");
        if (reserva == null) {
            reserva = new Reserva();
        }
        reserva.setFecha(fecha);
        session.setAttribute("currentReserva", reserva);
        return "redirect:/reserva/hora";
    }
    
    
    @GetMapping("/hora")
    public String reserva4(Model model, HttpSession session){
        Reserva reserva = (Reserva) session.getAttribute("currentReserva");
        if (reserva != null && reserva.getHora() != null) {
            model.addAttribute("horaSeleccionada", reserva.getHora());
        }
        return "reserva/ventana_reserva4";
    }

    @PostMapping("/guardarHora")
    public String guardarHora(@RequestParam("hora") String hora, HttpSession session) {
        Reserva reserva = (Reserva) session.getAttribute("currentReserva");
        if (reserva == null) {
            reserva = new Reserva();
        }
        reserva.setHora(hora);
        session.setAttribute("currentReserva", reserva);
        return "redirect:/reserva/datos";
    }

    @GetMapping("/datos")
    public String reserva5(HttpServletRequest request, Model model) {
        model.addAttribute("request", request);
        return "reserva/ventana_reserva5";
    }
}
