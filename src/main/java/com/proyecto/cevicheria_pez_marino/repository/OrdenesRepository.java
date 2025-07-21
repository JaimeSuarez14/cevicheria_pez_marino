package com.proyecto.cevicheria_pez_marino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.cevicheria_pez_marino.model.Ordenes;

@Repository
public interface OrdenesRepository extends JpaRepository<Ordenes, Integer> {
    
}
