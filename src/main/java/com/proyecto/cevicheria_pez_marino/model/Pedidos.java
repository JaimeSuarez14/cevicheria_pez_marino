package com.proyecto.cevicheria_pez_marino.model;

import jakarta.persistence.ManyToOne;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Data
@Entity
@Table(name = "pedidos")
public class Pedidos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombreProducto;
    private double precio;
    private double cantidad;
    private double subtotal;
    @ManyToOne
    private Ordenes orden;
    @ManyToOne
    private Producto producto;
}

