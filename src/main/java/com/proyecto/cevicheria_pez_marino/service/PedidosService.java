package com.proyecto.cevicheria_pez_marino.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.cevicheria_pez_marino.model.Pedidos;
import com.proyecto.cevicheria_pez_marino.repository.PedidosRepository;

@Service
public class PedidosService {
    @Autowired
    private PedidosRepository pedidoRepository;

    public List<Pedidos> findAll() {
        return pedidoRepository.findAll();
    }

    public Pedidos save(Pedidos pedido) {
        return pedidoRepository.save(pedido);
    }

    public Optional<Pedidos> findById(Integer id) {
        return pedidoRepository.findById(id);
    }

    public void delete(Integer id) {
        pedidoRepository.deleteById(id);
    }
}
