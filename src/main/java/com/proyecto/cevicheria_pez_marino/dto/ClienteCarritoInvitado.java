package com.proyecto.cevicheria_pez_marino.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteCarritoInvitado {
    private String nombre;
    private String apellido;
    private String correo;
    private String celular;
    private String calle;
    private String distrito;
    private String infoAdicional;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public void setCelular(String celular) {
        this.celular = celular;
    }
    public void setCalle(String calle) {
        this.calle = calle;
    }
    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }
    public String getNombre() {
        return nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public String getCorreo() {
        return correo;
    }
    public String getCelular() {
        return celular;
    }
    public String getCalle() {
        return calle;
    }
    public String getDistrito() {
        return distrito;
    }
    public String getInfoAdicional() {
        return infoAdicional;
    }
    public void setInfoAdicional(String infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    public ClienteCarritoInvitado copiar(){
        ClienteCarritoInvitado copia = new ClienteCarritoInvitado();
        copia.setNombre(this.nombre);
        copia.setApellido(this.apellido);
        copia.setCalle(this.calle);
        copia.setCelular(this.celular);
        copia.setCorreo(this.correo);
        copia.setDistrito(this.distrito);
        copia.setInfoAdicional(this.infoAdicional);

        return copia;
    }
}
