package com.proyecto.cevicheria_pez_marino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.cevicheria_pez_marino.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
}
