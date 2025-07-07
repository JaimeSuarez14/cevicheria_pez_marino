package com.proyecto.cevicheria_pez_marino.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.proyecto.cevicheria_pez_marino.model.Usuario;
import com.proyecto.cevicheria_pez_marino.service.UsuarioService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private static final Logger Logger = LoggerFactory.getLogger(UsuarioController.class);

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, 
                       @RequestParam(value = "continue", required = false) String continueUrl, 
                       Model model) {
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }
        if (continueUrl != null) {
            model.addAttribute("continueUrl", continueUrl);
        }
        return "usuario/login";
    }

    @GetMapping("/registro")
    public String registro(HttpServletRequest request, Model model) {
        Logger.info("Mostrando formulario de registro");
        model.addAttribute("request", request);
        model.addAttribute("usuario", new Usuario());
        return "usuario/registro";
    }

    @PostMapping("/registrarUsuario")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {
        try {
            Logger.info("Datos recibidos para registro:");
            Logger.info("Nombre: " + usuario.getNombre());
            Logger.info("Username: " + usuario.getUsername());
            Logger.info("Email: " + usuario.getEmail());
            Logger.info("Teléfono: " + usuario.getTelefono());
            Logger.info("Dirección: " + usuario.getDireccion());
            Logger.info("Contraseña: " + usuario.getPassword());
            Logger.info("Rol: " + usuario.getRol());

            // Validaciones básicas
            if (StringUtils.isBlank(usuario.getNombre()) ||
                StringUtils.isBlank(usuario.getUsername()) ||
                StringUtils.isBlank(usuario.getDireccion()) ||
                StringUtils.isBlank(usuario.getTelefono()) ||
                StringUtils.isBlank(usuario.getPassword())) {
                model.addAttribute("error", "Todos los campos son obligatorios");
                return "usuario/registro";
            }

            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                model.addAttribute("error", "El email es obligatorio");
                return "usuario/registro";
            }

            // Validación de formato de email
            String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            if (!usuario.getEmail().matches(emailPattern)) {
                model.addAttribute("error", "El formato del email no es válido");
                return "usuario/registro";
            }

            // Validación de longitud del username
            if (usuario.getUsername().length() < 3 || usuario.getUsername().length() > 20) {
                model.addAttribute("error", "El username debe tener entre 3 y 20 caracteres");
                return "usuario/registro";
            }

            // Validación de longitud de la contraseña
            if (usuario.getPassword().length() < 6) {
                model.addAttribute("error", "La contraseña debe tener al menos 6 caracteres");
                return "usuario/registro";
            }

            // Validación de número de teléfono
            if (!usuario.getTelefono().matches("\\d{9}")) {
                model.addAttribute("error", "El número de teléfono debe tener 9 dígitos");
                return "usuario/registro";
            }

            // Verificar si el email ya existe
            Optional<Usuario> usuarioOptional = usuarioService.findByEmail(usuario.getEmail());
            if (usuarioOptional.isPresent()) {
                model.addAttribute("error", "El email ya está registrado");
                return "usuario/registro";
            }

            // Verificar si el username ya existe
            Optional<Usuario> usuarioUsername = usuarioService.buscarPorUsername(usuario.getUsername());
            if (usuarioUsername.isPresent()) {
                model.addAttribute("error", "El nombre de usuario ya está registrado");
                return "usuario/registro";
            }

            // Si todo está bien, guardar el usuario
            Usuario usuarioGuardado = usuarioService.save(usuario);
            Logger.info("Usuario guardado con ID: " + usuarioGuardado.getId());
            model.addAttribute("mensaje", "Usuario registrado exitosamente. Por favor inicia sesión.");
            return "redirect:/usuario/login";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocurrió un error al registrar el usuario. Por favor intenta nuevamente.");
            return "usuario/registro";
        }
    }
    
}
