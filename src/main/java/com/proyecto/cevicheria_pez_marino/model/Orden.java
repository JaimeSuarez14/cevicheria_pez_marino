package com.proyecto.cevicheria_pez_marino.model;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Data
@Entity
@Table(name = "orden")
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fecha;
    private double total;
    private String estado;
    private String metodoPago;
    @ManyToOne
    private Usuario usuario;
    @OneToMany(mappedBy = "orden")
    private List<Pedido> pedido;
}
