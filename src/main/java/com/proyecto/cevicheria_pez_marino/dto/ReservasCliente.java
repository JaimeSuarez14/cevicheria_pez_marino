package com.proyecto.cevicheria_pez_marino.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservasCliente {
    private int id;
    private String nombre;
    private String correo;
    private String celular;
    private int numPersonas;
    private String fecha;
    private String hora;
    private String experiencia;
    private String medioPago;
    private double total;
    
    public List<ReservasCliente> buscarPorNombre(List<ReservasCliente> lista,String filtro) {
        //vamos ha convertir a minusculas para que la busqueda sea insensible a mayusculas y minusculas
        String palabraBuscada = filtro.toLowerCase();
        
        return lista.stream()
            .filter(p -> p.getNombre().toLowerCase().contains(palabraBuscada))
            .collect(Collectors.toList());
    }
}
