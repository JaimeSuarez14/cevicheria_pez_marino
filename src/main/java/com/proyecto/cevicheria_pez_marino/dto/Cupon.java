package com.proyecto.cevicheria_pez_marino.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class Cupon {

    private String codigo;
    private double descuento;
    private boolean aplicado;

    public Cupon(String codigo, double descuento) {
        this.codigo = codigo;
        this.descuento = descuento;
        this.aplicado = false;
    }
    
}