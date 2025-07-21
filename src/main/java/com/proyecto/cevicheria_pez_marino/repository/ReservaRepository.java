package com.proyecto.cevicheria_pez_marino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.cevicheria_pez_marino.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    // List<Reserva> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
}
