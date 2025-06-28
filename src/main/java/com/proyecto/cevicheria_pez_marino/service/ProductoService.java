package com.proyecto.cevicheria_pez_marino.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.proyecto.cevicheria_pez_marino.model.Producto;
import com.proyecto.cevicheria_pez_marino.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {
    
    private final ProductoRepository productoRepository;

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public Optional<Producto> findById(Integer id) {
        return productoRepository.findById(id);
    }

    public void delete(Integer id) {
        productoRepository.deleteById(id);
    }

    
    public List<Producto> buscarPorNombre(String busqueda) {
        //vamos ha convertir a minusculas para que la busqueda sea insensible a mayusculas y minusculas
        String palabraBuscada = busqueda.toLowerCase();
        
        return productoRepository.findAll().stream()
            .filter(p -> p.getNombre().toLowerCase().contains(palabraBuscada))
            .collect(Collectors.toList());
    }

    public List<Producto> buscarCategoria(String cat) {
        return productoRepository.findByCategoria(cat);
    }
}