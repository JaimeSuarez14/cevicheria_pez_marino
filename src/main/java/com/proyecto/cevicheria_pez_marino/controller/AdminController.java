package com.proyecto.cevicheria_pez_marino.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.proyecto.cevicheria_pez_marino.service.ProductoService;
import com.proyecto.cevicheria_pez_marino.service.UploadFileService;
import com.proyecto.cevicheria_pez_marino.service.UsuarioService;
import com.proyecto.cevicheria_pez_marino.model.Producto;
import com.proyecto.cevicheria_pez_marino.model.Usuario;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/administracion") 
@RequiredArgsConstructor
public class AdminController {
    
    private final ProductoService productoService;
    
    private final UsuarioService usuarioService;

    private final UploadFileService upload;
    
    @GetMapping("/home")
    public String paginalAdmistrador(){
        return "administracion/home";
    }

    @GetMapping("/lista_productos")
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "administracion/listaProductos";
    }
    
    @GetMapping("/verFormularioCrear")
    public String irFormularioCrearProducto() {
        return "administracion/formularioProductos" ;
    }

    @PostMapping("/guardarProducto")
    public String guardarProducto(Producto producto, @RequestParam("file") MultipartFile file ) throws IOException {

        //Obtenemos la session del usuario
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "Usuario no autenticado";
        }

        //obtenemos el username
        String username = authentication.getName();
        
        //obtenmos el usuario
        Optional<Usuario> usuarioOptional = usuarioService.buscarPorUsername(username);
        Usuario usuario = usuarioOptional.get(); 
        
        //obtenmos la imagen
        String imagen = upload.saveImage(file);

        //Creamos un nuevo producto
        Producto productoGuardar = new Producto();

        //Guardamos los datos del Producto Nuevo
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
    

    @GetMapping("/verActualizarProducto")
    public String actualizarProducto(){
        return "redirect:/administracion/lista_productos";
    }

    @GetMapping("/eliminarProducto")
    public String eliminarProducto(){
        return "redirect:/administracion/lista_productos";
    }
}
