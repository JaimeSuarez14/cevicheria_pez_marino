package com.proyecto.cevicheria_pez_marino.controller;

import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyecto.cevicheria_pez_marino.dto.UsuarioCliente;
import com.proyecto.cevicheria_pez_marino.model.Reserva;
import com.proyecto.cevicheria_pez_marino.model.Usuario;
import com.proyecto.cevicheria_pez_marino.service.ReservaService;
import com.proyecto.cevicheria_pez_marino.service.UsuarioService;
import io.micrometer.common.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/reserva")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;
    private final UsuarioService usuarioService;

    @GetMapping("/degustacion")
    public String reserva(Model model, HttpSession session) {
        // Obtener o crear reserva manteniendo datos existentes
        Reserva reserva = (Reserva) session.getAttribute("currentReserva");
        if (reserva == null) {
            reserva = new Reserva();
        }

        // Si ya tenía experiencia, mantenerla
        if (reserva.getExperiencia() != null) {
            model.addAttribute("experienciaSeleccionada", reserva.getExperiencia());
        }

        session.setAttribute("currentReserva", reserva);
        return "reserva/ventana_reserva1";
    }

    @PostMapping("/guardarExperiencia")
    public String guardarExperiencia(
            @RequestParam("experiencia") String experiencia,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Validación básica
        if (experiencia == null || experiencia.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Debe seleccionar una experiencia");
            return "redirect:/reserva/degustacion";
        }

        Reserva reserva = (Reserva) session.getAttribute("currentReserva");
        if (reserva == null) {
            reserva = new Reserva();
        }

        // Guardar la experiencia
        reserva.setExperiencia(experiencia);
        session.setAttribute("currentReserva", reserva);

        return "redirect:/reserva/personas";
    }

    @GetMapping("/personas")
    public String reserva2(Model model, HttpSession session) {
        Reserva reserva = (Reserva) session.getAttribute("currentReserva");

        // Si no hay reserva o experiencia, volver al inicio
        if (reserva == null || reserva.getExperiencia() == null) {
            return "redirect:/reserva/degustacion";
        }

        // Mantener el valor por defecto solo si no existe
        if (reserva.getNumPersonas() <= 0) {
            reserva.setNumPersonas(2);
        }

        model.addAttribute("numPersonas", reserva.getNumPersonas());
        return "reserva/ventana_reserva2";
    }

    @PostMapping("/guardarPersonas")
    public String guardarPersonas(@RequestParam("numPersonas") int numPersonas, HttpSession session) {
        Reserva reserva = (Reserva) session.getAttribute("currentReserva");
        if (reserva == null || reserva.getExperiencia() == null) {
            // Esto no debería pasar si siempre empezamos por /degustacion
            return "redirect:/reserva/degustacion";
        }
        reserva.setNumPersonas(numPersonas);
        session.setAttribute("currentReserva", reserva);
        return "redirect:/reserva/fecha";
    }

    @GetMapping("/fecha")
    public String reserva3(Model model, HttpSession session) {
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
    public String reserva4(Model model, HttpSession session) {
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
    public String reserva5(HttpServletRequest request, Model model, HttpSession session,@AuthenticationPrincipal Usuario userAtenticado) {
        // redirigir el login cuando se inicia sesion
        model.addAttribute("request", request);

        // Recuperar la reserva de la sesión
        Reserva reserva = (Reserva) session.getAttribute("currentReserva");
        if (reserva == null) {
            // Si por alguna razón no hay reserva en sesión, redirigir al inicio del flujo
            return "redirect:/reserva/degustacion";
        }

        UsuarioCliente usuarioAutenticado = (UsuarioCliente) session.getAttribute("usuarioSession");

        if (usuarioAutenticado != null) {
            model.addAttribute("usuarioSession", usuarioAutenticado);

            Optional<Usuario> usuarioUsername = usuarioService.buscarPorUsername(usuarioAutenticado.getUsername());
            if (!usuarioUsername.isPresent()) {
                model.addAttribute("error", "El nombre de usuario no está registrado");
                return "redirect:/reserva/degustacion";
            }
            
        }else if(userAtenticado != null) {
            model.addAttribute("usuarioSession", userAtenticado);
            Optional<Usuario> usuarioUsername = usuarioService.buscarPorUsername(userAtenticado.getUsername());
            if (!usuarioUsername.isPresent()) {
                model.addAttribute("error", "El nombre de usuario no está registrado");
                return "redirect:/reserva/degustacion";
            }
            
        }

        model.addAttribute("reserva", reserva);

        return "reserva/ventana_reserva5";
    }

    @PostMapping("/confirmarReserva")
    public String confirmarReserva(
            @RequestParam("nombre") String nombre,
            @RequestParam("correo") String email,
            @RequestParam("celular") String celular,
            @RequestParam("medioPago") String medioPago, // Necesitarás un input oculto o select para esto
            HttpSession session, Model model, RedirectAttributes redirectAttributes) {

        if (StringUtils.isBlank(nombre) ||
                StringUtils.isBlank(email) ||
                StringUtils.isBlank(celular) ||
                StringUtils.isBlank(medioPago)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Todos los campos son obligatorios");
            return "redirect:/reserva/datos";
        }

        Reserva reserva = (Reserva) session.getAttribute("currentReserva");

        if (reserva == null) {
            // Si la sesión expiró o no hay reserva, redirigir al inicio
            return "redirect:/reserva/degustacion";
        }

        if (reserva.getExperiencia() == null) {
            reserva.setExperiencia("Sin experiencia");
        }

        // Llenar los datos personales finales y el medio de pago
        reserva.setNombre(nombre);
        reserva.setCorreo(email);
        reserva.setCelular(celular);
        reserva.setMedioPago(medioPago);

        UsuarioCliente usuarioAutenticado = (UsuarioCliente) session.getAttribute("usuarioSession");

        // Asociar el usuario autenticado si existe
        if (usuarioAutenticado != null) {
            Optional<Usuario> usuarioUsername = usuarioService.buscarPorUsername(usuarioAutenticado.getUsername());

            if (usuarioUsername.isPresent()) {
                reserva.setUsuario(usuarioUsername.get());
            } else {
                // Si no se encuentra el usuario autenticado, se busca el usuario genérico
                Optional<Usuario> generico = usuarioService.buscarPorUsername("generico");

                // Verificamos que el usuario genérico esté presente antes de asignarlo
                if (generico.isPresent()) {
                    reserva.setUsuario(generico.get());
                } else {
                    // Manejo de error si el usuario genérico no se encuentra
                    throw new IllegalStateException("Usuario genérico no encontrado.");
                }
            }
        } else {
            // Manejo si no hay usuario autenticado
            Optional<Usuario> generico = usuarioService.buscarPorUsername("generico");

            // Verificamos que el usuario genérico esté presente antes de asignarlo
            if (generico.isPresent()) {
                reserva.setUsuario(generico.get());
            } else {
                // Manejo de error si el usuario genérico no se encuentra
                throw new IllegalStateException("Usuario genérico no encontrado.");
            }
        }

        // Lógica para calcular el total (puedes ajustar esto según tus precios de
        // experiencia)
        // Por ahora, un valor fijo o basado en numPersonas, experiencia, etc.
        reserva.setTotal(calcularTotalReserva(reserva)); // Implementa esta función

        try {
            reservaService.guardarReserva(reserva);
            session.removeAttribute("currentReserva"); // Limpiar la reserva de la sesión
            model.addAttribute("mensajeExito", "¡Reserva confirmada con éxito!");
            return "reserva/reserva_confirmada"; // Una nueva página para mostrar la confirmación
        } catch (Exception e) {
            System.err.println("Error al guardar la reserva: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Hubo un error al procesar tu reserva. Por favor, inténtalo de nuevo.");
            return "redirect:/reserva/datos";
            // Volver a la página de datos con un mensaje de error
        }
    }

    private double calcularTotalReserva(Reserva reserva) {
        // Ejemplo simple: 10.0 por persona + un extra por experiencia
        double total = reserva.getNumPersonas() * 10.0;
        if (reserva.getExperiencia() != null) {
            if (reserva.getExperiencia().contains("Degustación")) {
                total += 50.0; // Costo extra por degustación
            }
            // Agrega más condiciones según tus experiencias
        }
        return total;
    }
}
