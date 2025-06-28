package com.proyecto.cevicheria_pez_marino;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.proyecto.cevicheria_pez_marino.service.UsuarioService;

@SpringBootApplication
public class CevicheriaPezMarinoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CevicheriaPezMarinoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(UsuarioService usuarioService) {
		return args -> {
			// Crear administrador por defecto si no existe
			usuarioService.crearAdminPorDefecto();
			System.out.println("Aplicación iniciada. Usuario admin creado/verificado.");
		};
	}
}
