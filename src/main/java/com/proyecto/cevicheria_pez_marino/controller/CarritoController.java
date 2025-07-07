package com.proyecto.cevicheria_pez_marino.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyecto.cevicheria_pez_marino.dto.Cupon;
import com.proyecto.cevicheria_pez_marino.dto.ListaCarrito;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final Logger logger = LoggerFactory.getLogger(CarritoController.class);

    @PostMapping("/productos")
    @ResponseBody
    public ResponseEntity<Map<String, String>> procesarCarritoDesdeFrontend(
            @RequestBody List<ListaCarrito> listaCarritoRecibida,
            HttpSession session) {
        logger.info("Carrito recibido del frontend vía POST: " + listaCarritoRecibida);

        session.setAttribute("listaCarrito", listaCarritoRecibida);

        return ResponseEntity.ok(Collections.singletonMap("mensaje", "Carrito procesado con éxito!"));
    }

    @GetMapping("/productos") // Para mostar la ventana de carrito
    public String verVentanaProductos(Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        @SuppressWarnings("unchecked")
        List<ListaCarrito> listaDeLaSession = (List<ListaCarrito>) session.getAttribute("listaCarrito");

        if (listaDeLaSession == null || listaDeLaSession.isEmpty()) {
            listaDeLaSession = new ArrayList<>(); // Si no hay carrito en sesión, inicializa una lista vacía para
            redirectAttributes.addFlashAttribute("mensajeNotificacion", "Carrito vacío, por favor agregue productos.");
            redirectAttributes.addFlashAttribute("tipoNotificacion", "danger"); // Para que sea rojo
            return "redirect:/principal/menu";        // mostrar.
        }

        model.addAttribute("listaCarrito", listaDeLaSession);

        return "Carrito/productos";
    }

    // Para mantener la persistencia del carrito en el menu de comidas
    @GetMapping("/obtenerCarrito")
    @ResponseBody
    public ResponseEntity<List<ListaCarrito>> enviarCarrito(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<ListaCarrito> lista = (List<ListaCarrito>) session.getAttribute("listaCarrito");

        if (lista == null) {
            lista = new ArrayList<>();
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/elimarPCarrito/{id}")
    public String eliminarProductoCarrito(@PathVariable("id") int id, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<ListaCarrito> lista = (List<ListaCarrito>) session.getAttribute("listaCarrito");

        Iterator<ListaCarrito> iterator = lista.iterator();
        while (iterator.hasNext()) {
            ListaCarrito item = iterator.next();
            if (item.getProducto().getId() == id) {
                iterator.remove(); // Eliminar el elemento
                break; // Salir del bucle si se encontró y eliminó el elemento
            }
        }

        session.setAttribute("listaCarrito", lista);

        return "redirect:/carrito/productos";
    }

    @PostMapping("/aplicarCupon")

    public String aplicarCupon(@RequestParam String codigo, HttpSession session, RedirectAttributes redirectAttributes) {
        @SuppressWarnings("unchecked")
        List<ListaCarrito> lista = (List<ListaCarrito>) session.getAttribute("listaCarrito");

        if (lista == null || lista.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "El carrito está vacío.");
            return "redirect:/carrito/productos";
        }

        Cupon cuponAplicar = null;

        if (codigo.equals("comerico07")) {
            cuponAplicar = new Cupon(codigo, 10); // Ejemplo de cupón válido
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Código Incorrecto");
            return "redirect:/carrito/productos";
        }

        // Asumimos que aplicamos el cupón al primer producto del carrito
        ListaCarrito primerProducto = lista.get(0);
        if (primerProducto.getCupon() != null && primerProducto.getCupon().isAplicado()) {
            redirectAttributes.addFlashAttribute("mensaje", "El cupón ya ha sido aplicado.");
            ;
        } else {
            primerProducto.aplicarCupon(cuponAplicar);
            cuponAplicar.setAplicado(true); 
            redirectAttributes.addFlashAttribute("mensaje", "Cupón aplicado con éxito: " + cuponAplicar.getCodigo());
            session.setAttribute("listaCarrito", lista);
        }

        return "redirect:/carrito/productos";
    }

    @GetMapping("/cliente")
    public String verVentanaCliente(HttpServletRequest request, Model model) {
        model.addAttribute("request", request); 
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
