package com.proyecto.cevicheria_pez_marino.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.util.Optional;

import io.micrometer.common.util.StringUtils;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyecto.cevicheria_pez_marino.service.ProductoService;
import com.proyecto.cevicheria_pez_marino.service.ReservaService;
import com.proyecto.cevicheria_pez_marino.service.UploadFileService;
import com.proyecto.cevicheria_pez_marino.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

import com.proyecto.cevicheria_pez_marino.dto.ReservasCliente;
import com.proyecto.cevicheria_pez_marino.dto.UsuarioCliente;
import com.proyecto.cevicheria_pez_marino.model.Producto;
import com.proyecto.cevicheria_pez_marino.model.Reserva;
import com.proyecto.cevicheria_pez_marino.model.Usuario;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/administracion")
@RequiredArgsConstructor
public class AdminController {
    private final ReservaService reservaService;

    private final ProductoService productoService;

    private final UsuarioService usuarioService;

    private final UploadFileService upload;

    @GetMapping("/home")
    public String paginalAdmistrador() {
        return "administracion/home";
    }

    @GetMapping("/lista_productos")
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "administracion/listaProductos";
    }

    @GetMapping("/verFormularioCrear")
    public String irFormularioCrearProducto() {
        return "administracion/formularioProductos";
    }

    @PostMapping("/guardarProducto")
    public String guardarProducto(Producto producto, @RequestParam("file") MultipartFile file) throws IOException {

        // Obtenemos la session del usuario
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "Usuario no autenticado";
        }

        // obtenemos el username
        String username = authentication.getName();

        // obtenemos el usuario
        Optional<Usuario> usuarioOptional = usuarioService.buscarPorUsername(username);
        Usuario usuario = usuarioOptional.get();

        // obtenemos la imagen
        String imagen = upload.saveImage(file);

        // Creamos un nuevo producto
        Producto productoGuardar = new Producto();

        // Guardamos los datos del Producto Nuevo
        productoGuardar.setNombre(producto.getNombre());
        productoGuardar.setDescripcion(producto.getDescripcion());
        productoGuardar.setCategoria(producto.getCategoria());
        productoGuardar.setPrecio(producto.getPrecio());
        productoGuardar.setStock(producto.getStock());
        productoGuardar.setUsuario(usuario);
        productoGuardar.setImagen(imagen);

        productoService.save(productoGuardar);

        return "redirect:/administracion/lista_productos";
    }

    @GetMapping("/verProducto/{id}")
    @ResponseBody
    public ResponseEntity<Producto> enviarProducto(@PathVariable("id") int id) {
        Optional<Producto> productoOptional = productoService.findById(id);

        if (!productoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Producto producto = productoOptional.get();

        return ResponseEntity.ok(producto);
    }

    @PostMapping("/actualizarProducto/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> actualizarProducto(@PathVariable("id") int id, Producto producto,
            @RequestParam(value = "file", required = false) MultipartFile file, HttpSession session)
            throws IOException {

        Optional<Producto> productoOptional = productoService.findById(id);

        // Validamos que el producto exista
        if (!productoOptional.isPresent()) {
            session.setAttribute("errorMensaje", "El producto no existe");
            return ResponseEntity.badRequest().body(Map.of("error", "El producto no existe"));
        }

        Producto productoActualizar = productoOptional.get();

        // Validamos el nombre no se repita
        if (!productoActualizar.getNombre().equals(producto.getNombre())) {
            boolean productoNombreOptional = productoService.existsByNombre(producto.getNombre());
            if (productoNombreOptional) {
                session.setAttribute("errorMensaje", "El nombre del producto ya existe");
                return ResponseEntity.badRequest().body(Map.of("error", "El nombre del producto ya existe"));
            }
        }

        // Obtenemos la session del usuario
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            session.setAttribute("errorMensaje", "Usuario no autenticado");
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario no autenticado"));
        }
        // obtenemos el username
        String username = authentication.getName();
        // obtenmos el usuario
        Optional<Usuario> usuarioOptional = usuarioService.buscarPorUsername(username);
        Usuario usuario = usuarioOptional.get();

        productoActualizar.setNombre(producto.getNombre());
        productoActualizar.setDescripcion(producto.getDescripcion());
        productoActualizar.setCategoria(producto.getCategoria());
        productoActualizar.setPrecio(producto.getPrecio());
        productoActualizar.setStock(producto.getStock());
        productoActualizar.setUsuario(usuario);

        if (!file.isEmpty()) {
            upload.deleteImage(productoActualizar.getImagen());
            productoActualizar.setImagen(upload.saveImage(file));
        }

        productoService.save(productoActualizar);

        session.setAttribute("conformidadMensaje", "Producto actualizado correctamente");

        return ResponseEntity.ok(Map.of("success", "Producto actualizado correctamente"));
    }

    @GetMapping("/eliminarProducto/{id}")
    public String eliminarProducto(@PathVariable("id") int id, HttpSession session) {
        Optional<Producto> productoOptional = productoService.findById(id);

        // Validamos que el producto exista
        if (!productoOptional.isPresent()) {
            session.setAttribute("errorMensaje", "El producto no existe");
            return "redirect:/administracion/lista_productos";
        }

        Producto productoEliminar = productoOptional.get();

        productoService.eliminarProducto(productoEliminar.getId());

        session.setAttribute("conformidadMensaje", "Producto eliminado correctamente");

        return "redirect:/administracion/lista_productos";
    }

    @GetMapping("/lista_usuarios")
    public String getMethodName(Model model) {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        List<UsuarioCliente> usuariosCliente = usuarios.stream()
                .map(e -> new UsuarioCliente(e.getNombre(), e.getUsername(), e.getEmail(), e.getDireccion(),
                        e.getTelefono(), e.getRol()))
                .toList();
        model.addAttribute("usuarios", usuariosCliente);
        model.addAttribute("filtro", "todos");
        LocalDate fechaActual = LocalDate.now();
        model.addAttribute("fechaActualizacion", fechaActual);
        return "administracion/listaUsuarios";
    }
    
    @RequestMapping(value = "/filtroUsuarios", method = { RequestMethod.GET, RequestMethod.POST })
    public String filtrosdeReporte(@RequestParam() String filtro, Model model, RedirectAttributes redirectAttributes) {

        if (StringUtils.isBlank(filtro)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Todos los campos son obligatorios");
            return "redirect:/administracion/lista_usuarios";
        }

        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        List<UsuarioCliente> usuariosCliente = usuarios.stream()
                .map(e -> new UsuarioCliente(e.getNombre(), e.getUsername(), e.getEmail(), e.getDireccion(),
                        e.getTelefono(), e.getRol()))   
                .toList();
        if (filtro.equals("admin")) {
            List<UsuarioCliente> usu = usuariosCliente.stream()
                    .filter(p -> p.getRol().equals("ADMIN")) // Filtrar productos agotados
                    .toList(); // Convertir a lista
            model.addAttribute("usuarios", usu);
            model.addAttribute("filtro", filtro);

        } else if (filtro.equals("todos")) {
            model.addAttribute("usuarios", usuariosCliente);
            model.addAttribute("filtro", filtro);

        } else if (filtro.equals("user")) {
            List<UsuarioCliente> usu = usuariosCliente.stream()
                    .filter(p -> p.getRol().equals("USER")) // Filtrar productos agotados
                    .toList(); // Convertir a lista
            model.addAttribute("usuarios", usu);
            model.addAttribute("filtro", filtro);
        }else {
            UsuarioCliente usuarioC = new UsuarioCliente();
            List<UsuarioCliente> usu = usuarioC.buscarPorNombre(usuariosCliente,filtro);
            model.addAttribute("usuarios", usu);
            model.addAttribute("parametroUxiliar", "busqueda");
            model.addAttribute("filtro", filtro);
        }

        LocalDate fechaActual = LocalDate.now();
        model.addAttribute("fechaActualizacion", fechaActual);

        return "administracion/listaUsuarios";

    }

    @GetMapping("/dataUsuarios")
    @ResponseBody
    public ResponseEntity<List<UsuarioCliente>> enviarData() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        List<UsuarioCliente> data = usuarios.stream()
            .map(u -> new UsuarioCliente(
                u.getNombre(),
                u.getUsername(),
                u.getEmail(), 
                u.getDireccion(), 
                u.getTelefono(),
                u.getRol()))
            .toList();

        return ResponseEntity.ok(data);
    }

    @PostMapping("/actualizarUsuario/{usernameUpdate}")
    @ResponseBody
    public ResponseEntity<String> actualizarUsuario(
            @PathVariable String usernameUpdate, 
            @RequestBody UsuarioCliente usuarioDto) {
        try {
            Optional<Usuario> usuarioOptional = usuarioService.buscarPorUsername(usernameUpdate);
            if (usuarioOptional.isPresent()) {
                Usuario usuario = usuarioOptional.get();
                usuario.setNombre(usuarioDto.getNombre());
                usuario.setUsername(usuarioDto.getUsername());
                usuario.setDireccion(usuarioDto.getDireccion());
                usuario.setEmail(usuarioDto.getEmail());
                usuario.setTelefono(usuarioDto.getTelefono());;
                
                usuarioService.actualizar(usuario);
                return ResponseEntity.ok("Usuario actualizado");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/dataReservas")
    @ResponseBody
    public ResponseEntity<List<ReservasCliente>> enviarDataReservas() {
        List<Reserva> reservas = reservaService.obtenerTodosLasReservas();
        List<ReservasCliente> data = reservas.stream()
            .map(r -> new ReservasCliente(
                r.getId(),
                r.getNombre(),
                r.getCorreo(),
                r.getCelular(), 
                r.getNumPersonas(), 
                r.getFecha(),
                r.getHora(),
                r.getExperiencia(),
                r.getMedioPago(),
                r.getTotal()))
            .toList();

        return ResponseEntity.ok(data);
    }
}
