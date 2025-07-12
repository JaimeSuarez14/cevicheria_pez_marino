package com.proyecto.cevicheria_pez_marino.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedioPago {
    private String direccionFacturacion;
    private String numeroTajeta;
    private String anoVencimiento;
    private String mesVencimiento;
    private String codigo;
}   
