package com.proyecto.cevicheria_pez_marino.dto;

import com.proyecto.cevicheria_pez_marino.model.Producto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListaCarrito implements Serializable {
    private static final long serialVersionUID = 1L;
    private Producto producto;
    private int cantidad;
    private Cupon cupon;

    public void aplicarCupon(Cupon c){
        this.cupon = c;

        if (c != null) {
            double descuento = c.getDescuento();
            double precioOriginal = producto.getPrecio();
            double precioConDescuento = precioOriginal * (1 - (descuento / 100));
            producto.setPrecio(precioConDescuento); // 
        }
    }
}
