package com.proyecto.cevicheria_pez_marino.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCliente {
    private String nombre;

    private String username;

    private String email;

    private String direccion;

    private String telefono;

    private String rol;

    public List<UsuarioCliente> buscarPorNombre(List<UsuarioCliente> lista,String busqueda) {
        //vamos ha convertir a minusculas para que la busqueda sea insensible a mayusculas y minusculas
        String palabraBuscada = busqueda.toLowerCase();
        
        return lista.stream()
            .filter(p -> p.getNombre().toLowerCase().contains(palabraBuscada))
            .collect(Collectors.toList());
    }
}
