package com.proyecto.cevicheria_pez_marino.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;


@Data
@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String correo;
    private String celular;
    private int numPersonas;
    private String fecha;
    private String hora;
    private String experiencia;
    @ManyToOne
    private Usuario usuario;
    private String medioPago;
    private double total;
    
}
