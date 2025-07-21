package com.proyecto.cevicheria_pez_marino.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.cevicheria_pez_marino.model.Ordenes;
import com.proyecto.cevicheria_pez_marino.model.Pedidos;
import com.proyecto.cevicheria_pez_marino.repository.OrdenesRepository;

@Service
public class OrdenesService {
    @Autowired
    private OrdenesRepository ordenesRepository;

    public List<Ordenes> findAll() {
        return ordenesRepository.findAll();
    }

    public Ordenes save(Ordenes orden) {
        return ordenesRepository.save(orden);
    }

    public Optional<Ordenes> findById(Integer id) {
        return ordenesRepository.findById(id);
    }

    public void delete(Integer id) {
        ordenesRepository.deleteById(id);
    }

    //calcular el total de la orden
    public double calcularTotal(List<Pedidos> pedidos) {
        return pedidos.stream().mapToDouble(Pedidos::getSubtotal).sum();
    }
}
