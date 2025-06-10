package com.proyecto.cevicheria_pez_marino.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.cevicheria_pez_marino.model.Orden;
import com.proyecto.cevicheria_pez_marino.repository.OrdenRepository;

@Service
public class OrdenService {
    @Autowired
    private OrdenRepository ordenRepository;

    public List<Orden> findAll() {
        return ordenRepository.findAll();
    }

    public Orden save(Orden orden) {
        return ordenRepository.save(orden);
    }

    public Optional<Orden> findById(Integer id) {
        return ordenRepository.findById(id);
    }

    public void delete(Integer id) {
        ordenRepository.deleteById(id);
    }
}
