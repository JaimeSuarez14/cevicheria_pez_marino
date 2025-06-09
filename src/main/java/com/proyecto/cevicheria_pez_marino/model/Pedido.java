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
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private double precio;
    private double cantidad;
    private double subtotal;
    @ManyToOne
    private Orden orden;
    @ManyToOne
    private Producto producto;
}
