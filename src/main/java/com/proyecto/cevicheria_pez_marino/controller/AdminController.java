package com.proyecto.cevicheria_pez_marino.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.proyecto.cevicheria_pez_marino.service.ProductoService;
import com.proyecto.cevicheria_pez_marino.service.UploadFileService;
import com.proyecto.cevicheria_pez_marino.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

import com.proyecto.cevicheria_pez_marino.model.Producto;
import com.proyecto.cevicheria_pez_marino.model.Usuario;

import lombok.RequiredArgsConstructor;


@Controller
//@RequestMapping("/administracion") 
@RequiredArgsConstructor
public class AdminController {
    
    private final ProductoService productoService;
    
    private final UsuarioService usuarioService;

    private final UploadFileService upload;
    

    @GetMapping("/administracion/home")
    public String paginalAdmistrador(){
        return "administracion/home";
    }

    @GetMapping("/administracion/lista_productos")
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "administracion/listaProductos";
    }
    
    @GetMapping("/administracion/verFormularioCrear")
    public String irFormularioCrearProducto() {
        return "administracion/formularioProductos" ;
    }

    @PostMapping("/administracion/guardarProducto")
    public String guardarProducto(Producto producto, @RequestParam("file") MultipartFile file ) throws IOException {

        //Obtenemos la session del usuario
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "Usuario no autenticado";
        }

        //obtenemos el username
        String username = authentication.getName();
        
        //obtenemos el usuario
        Optional<Usuario> usuarioOptional = usuarioService.buscarPorUsername(username);
        Usuario usuario = usuarioOptional.get(); 
        
        //obtenemos la imagen
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
    

    @GetMapping("/administracion/verProducto/{id}")
    @ResponseBody
    public ResponseEntity<Producto>  enviarProducto(@PathVariable("id") int id){
        Optional<Producto> productoOptional = productoService.findById(id);

        if(!productoOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }

        Producto producto = productoOptional.get();

        return ResponseEntity.ok(producto);
    }
    
    

    @PostMapping("/administracion/actualizarProducto/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> actualizarProducto(@PathVariable("id") int id, Producto producto, 
    @RequestParam(value = "file", required = false) MultipartFile file, HttpSession session) throws IOException {

        Optional<Producto> productoOptional = productoService.findById(id);

        //Validamos que el producto exista
        if(!productoOptional.isPresent()){
            session.setAttribute("errorMensaje", "El producto no existe");
            return ResponseEntity.badRequest().body(Map.of("error", "El producto no existe"));
        }

        Producto productoActualizar = productoOptional.get();

        //Validamos el nombre no se repita
        if (!productoActualizar.getNombre().equals(producto.getNombre())) {
            boolean productoNombreOptional = productoService.existsByNombre(producto.getNombre());
            if (productoNombreOptional) {
                session.setAttribute("errorMensaje", "El nombre del producto ya existe");
                return ResponseEntity.badRequest().body(Map.of("error", "El nombre del producto ya existe"));
            }
        }

        //Obtenemos la session del usuario
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            session.setAttribute("errorMensaje", "Usuario no autenticado");
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario no autenticado"));
        }
        //obtenemos el username
        String username = authentication.getName();
        //obtenmos el usuario
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


    @GetMapping("/administracion/eliminarProducto/{id}")
    public String eliminarProducto(@PathVariable("id") int id, HttpSession session){
        Optional<Producto> productoOptional = productoService.findById(id);

        //Validamos que el producto exista
        if(!productoOptional.isPresent()){
            session.setAttribute("errorMensaje", "El producto no existe");
            return "redirect:/administracion/lista_productos";
        }

        Producto productoEliminar = productoOptional.get();

        productoService.eliminarProducto(productoEliminar.getId());

        session.setAttribute("conformidadMensaje", "Producto eliminado correctamente");

        return "redirect:/administracion/lista_productos";
    }
}
