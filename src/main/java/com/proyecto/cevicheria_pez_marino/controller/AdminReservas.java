package com.proyecto.cevicheria_pez_marino.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyecto.cevicheria_pez_marino.dto.ReservasCliente;

import com.proyecto.cevicheria_pez_marino.model.Reserva;

import com.proyecto.cevicheria_pez_marino.service.ReservaService;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/adminReserva")
@RequiredArgsConstructor
public class AdminReservas {

    private final ReservaService reservaService;

    @GetMapping("/listaReservas")
    public String getMethodName(Model model) {

        List<Reserva> reservas = reservaService.obtenerTodosLasReservas();

        model.addAttribute("reservas", reservas);
        model.addAttribute("filtro", "todos");
        return "administracion/listaReservas";
    }

    @RequestMapping(value = "/filtroReservas", method = { RequestMethod.GET, RequestMethod.POST })
    public String filtrosdeReporte(@RequestParam() String filtro, Model model, RedirectAttributes redirectAttributes) {

        if (StringUtils.isBlank(filtro)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Todos los campos son obligatorios");
            return "redirect:/adminReservas/listaReservas";
        }

        List<Reserva> reservas = reservaService.obtenerTodosLasReservas();
        List<ReservasCliente> reservasCliente = reservas.stream()
                .map(e -> new ReservasCliente(e.getId(), e.getNombre(), e.getCorreo(), e.getCelular(),
                        e.getNumPersonas(), e.getFecha(),
                        e.getHora(), e.getExperiencia(), e.getMedioPago(), e.getTotal()))
                .toList();

        if (filtro.equals("todos")) {
            model.addAttribute("reservas", reservasCliente);
            model.addAttribute("filtro", filtro);

        } else {
            ReservasCliente reservaC = new ReservasCliente();
            List<ReservasCliente> usu = reservaC.buscarPorNombre(reservasCliente, filtro);
            model.addAttribute("reservas", usu);
            model.addAttribute("parametroUxiliar", "busqueda");
            model.addAttribute("filtro", filtro);
        }

        return "administracion/listaReservas";

    }


    @PostMapping("/actualizarReserva/{idUpdate}")
    @ResponseBody
    public ResponseEntity<String> actualizarReserva(@PathVariable int idUpdate, @RequestBody Reserva reservaDto) {

        try {
            Optional<Reserva> reservaOptional = reservaService.findById(idUpdate);
            
            if (reservaOptional.isPresent()) {
                Reserva reserva = reservaOptional.get();

                reserva.setCelular(reservaDto.getCelular());
                reserva.setNumPersonas(reservaDto.getNumPersonas());
                reserva.setFecha(reservaDto.getFecha());
                reserva.setHora(reservaDto.getHora());

                reservaService.guardarReserva(reserva);
                return ResponseEntity.ok("Reserva actualizada");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // @PostMapping("/filtrarPorFecha")
    // public String filtrarPorFecha(
    //         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
    //         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin,
    //         Model model) {

    //     // Validación básica de fechas
    //     if (fechaInicio == null || fechaFin == null) {
    //         model.addAttribute("error", "Ambas fechas son requeridas");
    //         return "administracion/listaReservas";
    //     }

    //     if (fechaFin.isBefore(fechaInicio)) {
    //         model.addAttribute("error", "La fecha final no puede ser anterior a la inicial");
    //         return "administracion/listaReservas";
    //     }

    //     // Filtrar directamente en la base de datos (más eficiente)
    //     List<Reserva> reservas = reservaService.filtrarPorFechas(fechaInicio, fechaFin);

    //     // Convertir a DTO
    //     List<ReservasCliente> resultados = reservas.stream()
    //             .map(r -> new ReservasCliente(
    //                     r.getId(), r.getNombre(), r.getCorreo(), r.getCelular(),
    //                     r.getNumPersonas(), r.getFecha(), r.getHora(),
    //                     r.getExperiencia(), r.getMedioPago(), r.getTotal()))
    //             .collect(Collectors.toList());

    //     // Agregar al modelo
    //     model.addAttribute("reservas", resultados);
    //     model.addAttribute("fechaInicio", fechaInicio);
    //     model.addAttribute("fechaFin", fechaFin);
    //     model.addAttribute("filtroFecha", true); // Para mostrar que se aplicó este filtro

    //     return "administracion/listaReservas";
    // }
}
