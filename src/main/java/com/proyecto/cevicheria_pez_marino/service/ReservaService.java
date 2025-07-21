package com.proyecto.cevicheria_pez_marino.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.cevicheria_pez_marino.model.Reserva;
import com.proyecto.cevicheria_pez_marino.repository.ReservaRepository;

@Service
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepository;

    public List<Reserva> obtenerTodosLasReservas() {
        return reservaRepository.findAll();
    }

    public Reserva guardarReserva(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public Optional<Reserva> findById(Integer id) {
        return reservaRepository.findById(id);
    }

    public void delete(Integer id) {
        reservaRepository.deleteById(id);
    }

    // public List<Reserva> filtrarPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
    //     return reservaRepository.findByFechaBetween(fechaInicio, fechaFin);
    // }
}
