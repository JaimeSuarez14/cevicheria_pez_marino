package com.proyecto.cevicheria_pez_marino.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyecto.cevicheria_pez_marino.dto.ClienteCarritoInvitado;
import com.proyecto.cevicheria_pez_marino.dto.Cupon;
import com.proyecto.cevicheria_pez_marino.dto.ListaCarrito;
import com.proyecto.cevicheria_pez_marino.model.Ordenes;
import com.proyecto.cevicheria_pez_marino.model.Pedidos;
import com.proyecto.cevicheria_pez_marino.model.Usuario;
import com.proyecto.cevicheria_pez_marino.service.OrdenesService;
import com.proyecto.cevicheria_pez_marino.service.PedidosService;
import com.proyecto.cevicheria_pez_marino.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final Logger logger = LoggerFactory.getLogger(CarritoController.class);

    private final UsuarioService usuarioService;

    private final PedidosService pedidosService;
    
    private final OrdenesService ordenesService;

    @PostMapping("/productos")
    @ResponseBody
    public ResponseEntity<Map<String, String>> procesarCarritoDesdeFrontend(
            @RequestBody List<ListaCarrito> listaCarritoRecibida,
            HttpSession session) {
        try {
            logger.info("Carrito recibido del frontend vía POST: " + listaCarritoRecibida);
            
            if (listaCarritoRecibida == null) {
                logger.error("Lista de carrito recibida es null");
                return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Error: Lista de carrito vacía"));
            }

            // Log detallado de cada elemento del carrito
            for (int i = 0; i < listaCarritoRecibida.size(); i++) {
                ListaCarrito item = listaCarritoRecibida.get(i);
                logger.info("Item " + i + ": producto=" + item.getProducto() + ", cantidad=" + item.getCantidad() + ", cupon=" + item.getCupon());
            }

            session.setAttribute("listaCarrito", listaCarritoRecibida);
            logger.info("Carrito guardado en sesión exitosamente");

            return ResponseEntity.ok(Collections.singletonMap("mensaje", "Carrito procesado con éxito!"));
        } catch (Exception e) {
            logger.error("Error al procesar el carrito: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Error al procesar el carrito: " + e.getMessage()));
        }
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
    public String verVentanaCliente(HttpServletRequest request, Model model, @AuthenticationPrincipal Usuario usuarioAutenticado, HttpSession session) {
        
        @SuppressWarnings("unchecked")
        List<ListaCarrito> lista = (List<ListaCarrito>) session.getAttribute("listaCarrito");

        if (lista == null || lista.isEmpty()) {
            return "Carrito/productos";
        }
        model.addAttribute("usuario", usuarioAutenticado);
        model.addAttribute("request", request); 
        return "Carrito/cliente";
    }

    @PostMapping("/cliente")
    public String GuardarInformacion(@RequestParam("correo") String correo,@RequestParam("celular") String celular, Model model, HttpSession session) {

        ClienteCarritoInvitado clienteInvitado = new ClienteCarritoInvitado();
        clienteInvitado.setCorreo(correo);
        clienteInvitado.setCelular(celular);

        session.setAttribute("clienteInvitado", clienteInvitado);
        
        return "Carrito/envio";
    }

    @GetMapping("/envio")
    public String verVentanaEnvio(@AuthenticationPrincipal Usuario usuarioAutenticado,Model model) {
        model.addAttribute("usuario", usuarioAutenticado);
        return "Carrito/envio";
    }

    @PostMapping("/envio")
    public String GuardarInformacionEnvio(@AuthenticationPrincipal Usuario usuarioAutenticado,@ModelAttribute ClienteCarritoInvitado clienteCarritoInvitado, HttpSession session ) {

        ClienteCarritoInvitado cliente = clienteCarritoInvitado.copiar();
        

        session.setAttribute("clienteInvitado", cliente);
        return "Carrito/pago";
    }


    @GetMapping("/pago")
    public String verVentanaPago(@AuthenticationPrincipal Usuario usuarioAutenticado,Model model) {
        model.addAttribute("usuario", usuarioAutenticado);
        return "Carrito/pago";
    }

    @PostMapping("/pago")
    public String guardarPago(@AuthenticationPrincipal Usuario usuarioAutenticado,Model model) {
        model.addAttribute("usuario", usuarioAutenticado);
        return "Carrito/pago";
    }


    @GetMapping("/finalizar")
    public String verVentanaFinalizar(HttpSession session,Model model, @AuthenticationPrincipal Usuario usuarioAutenticado) {
        ClienteCarritoInvitado cliente = (ClienteCarritoInvitado) session.getAttribute("clienteInvitado");

        boolean  usuarioLogueado = usuarioAutenticado != null;
        
        if( usuarioLogueado){
            model.addAttribute("usuario", usuarioAutenticado);
        }else{
            model.addAttribute("cliente", cliente);

        }

        @SuppressWarnings("unchecked")
        List<ListaCarrito> lista = (List<ListaCarrito>) session.getAttribute("listaCarrito");

        if (lista == null || lista.isEmpty()) {
            return "Carrito/productos";
        }
        
        return "Carrito/finalizar";
    }

    @SuppressWarnings("")
    @GetMapping("/resumenCompra")
    public String verVentanaResumenCompra(HttpSession session, Model model, @AuthenticationPrincipal Usuario usuarioAutenticado) {
        @SuppressWarnings("unchecked")
        List<ListaCarrito> lista = (List<ListaCarrito>) session.getAttribute("listaCarrito");

        if (lista == null || lista.isEmpty()) {
            return "Carrito/productos";
        }

        ClienteCarritoInvitado cliente = (ClienteCarritoInvitado) session.getAttribute("clienteInvitado");

        var  usuarioLogueado = usuarioAutenticado != null ? usuarioAutenticado: cliente ;
        
        if( usuarioLogueado != null){
            model.addAttribute("email", usuarioAutenticado!= null ? usuarioAutenticado.getEmail() : cliente.getCorreo());
        }

        //usuario de respaldo
        Usuario generico = usuarioService.buscarPorUsername("generico").orElse(null);


        //GUARDAR PEDIDO Y ORDEN
        // Paso 1: Crear la orden
        Ordenes orden = new Ordenes();
        orden.setFecha(LocalDate.now().toString());
        orden.setEstado("Pendiente");
        orden.setMetodoPago("Tarjeta");
        orden.setUsuario( usuarioAutenticado instanceof Usuario ? usuarioAutenticado : generico);

        // Guardar la orden en la base de datos (esto le asignará un ID)
        Ordenes ordenCompleta = ordenesService.save(orden);

        // Paso 2: Crear y guardar los pedidos asociados
        List<Pedidos> pedidos = new ArrayList<>();
        double total =0;
        for (ListaCarrito listas : lista) {
            Pedidos pedido = new Pedidos();
            pedido.setNombreProducto(listas.getProducto().getNombre());
            pedido.setPrecio(listas.getProducto().getPrecio());
            pedido.setCantidad(listas.getCantidad());
            double subtotal = listas.getCantidad() * listas.getProducto().getPrecio();
            total += subtotal;
            
            pedido.setSubtotal(subtotal);
            // Asociar el pedido a la orden
            pedido.setOrden(orden);
            pedido.setProducto(listas.getProducto());

            // Guardar el pedido en la base de datos
            pedido = pedidosService.save(pedido);

            // Agregar el pedido a la lista de pedidos
            pedidos.add(pedido);
        }

        // Paso 3: Asociar la lista de pedidos a la orden
        ordenCompleta.setTotal(total);        
        // Guardar la orden de nuevo para actualizar la lista de pedidos
        ordenesService.save(ordenCompleta);
        
        model.addAttribute("usuarioLogueado", usuarioAutenticado!= null ? true : false);
        
        model.addAttribute("lista", lista);
        session.removeAttribute("listaCarrito");   

        return "Carrito/resumenCompra";
    }

    @PostMapping("/registrarUsuario")
    public String crearUsuarioDespuesCarrito(@RequestParam("usuario") String usuario, @RequestParam("password") String password,
    HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        
        ClienteCarritoInvitado cliente = (ClienteCarritoInvitado) session.getAttribute("clienteInvitado");

        //redirectAttributes.addAttribute y se recibe en el argumento del metodo  public String detalle(@RequestParam("id")
        //redirectAttributes.addFlashAttribute  y automativamente se guarda en el model de la vista

        if (cliente.getCorreo() == null || cliente.getCorreo().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El email es obligatorio");
            return "redirect:/usuario/registro";
        }

        // Validación de formato de email
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (!cliente.getCorreo().matches(emailPattern)) {
            redirectAttributes.addFlashAttribute("error", "El formato del email no es válido");
            return "redirect:/usuario/registro";
        }

        // Validación de longitud del username
        if (usuario.length() < 3 || usuario.length() > 20) {
            redirectAttributes.addFlashAttribute("error", "El username debe tener entre 3 y 20 caracteres");
            return "redirect:/usuario/registro";
        }

        // Validación de longitud de la contraseña
        if (password.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "La contraseña debe tener al menos 6 caracteres");
            return "redirect:/usuario/registro";
        }

        // Validación de número de teléfono
        if (!cliente.getCelular().matches("\\d{9}")) {
            redirectAttributes.addFlashAttribute("error", "El número de teléfono debe tener 9 dígitos");
            return "redirect:/usuario/registro";
        }

        // Verificar si el email ya existe
        Optional<Usuario> usuarioOptional = usuarioService.findByEmail(cliente.getCorreo());
        if (usuarioOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El email ya está registrado");
            return "redirect:/usuario/registro";
        }

        // Verificar si el username ya existe
        Optional<Usuario> usuarioUsername = usuarioService.buscarPorUsername(usuario);
        if (usuarioUsername.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El nombre de usuario ya está registrado");
            return "redirect:/usuario/registro";
        }

        Usuario usuarioNuevo = new Usuario();
        String nombreCompleto = cliente.getNombre() + " " + cliente.getApellido();
        usuarioNuevo.setNombre(nombreCompleto);
        usuarioNuevo.setUsername(usuario);
        usuarioNuevo.setEmail(cliente.getCorreo());
        usuarioNuevo.setPassword(password);
        usuarioNuevo.setTelefono(cliente.getCelular());
        String direccion = cliente.getCalle() + " - " + cliente.getDistrito() + " - " + cliente.getInfoAdicional(); 
        usuarioNuevo.setDireccion(direccion);

        // Si todo está bien, guardar el usuario
        usuarioService.save(usuarioNuevo);

        return "redirect:/principal";
    }
    
    @GetMapping("/terminarProceso")
    public String terminarProceso(HttpSession session) {
        session.removeAttribute("clienteInvitado");
        session.removeAttribute("listaCarrito");   
        return "redirect:/principal";

    }
    
}
