package com.proyecto.cevicheria_pez_marino.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.cevicheria_pez_marino.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
