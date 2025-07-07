package com.proyecto.cevicheria_pez_marino.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario implements  UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;

    private String username;

    private String email;

    private String password;

    private String telefono;

    private String direccion;

    private String rol;

    // Constructores
    public Usuario() {}

    public Usuario(String nombre, String username, String email, String password, String telefono, String direccion, String tipo) {
        this.nombre = nombre;
        this.username = username;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rol = tipo;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return rol;
    }

    public void setTipo(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", tipo='" + rol + '\'' +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convierte tu campo 'rol' (String) a una colección de GrantedAuthority
        // Spring Security espera roles con el prefijo "ROLE_"
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.rol.toUpperCase()));
    }

    @Override
    @Transient // <--- ¡IMPORTANTE! Para que JPA no intente persistir este método como columna
    public String getPassword() {
        return password; // Devuelve la contraseña encriptada
    }

    @Override
    @Transient // <--- ¡IMPORTANTE! Para que JPA no intente persistir este método como columna
    public String getUsername() {
        return username; // Devuelve el nombre de usuario
    }

    // Métodos de cuenta (generalmente se devuelven true a menos que tengas lógica de bloqueo/expiración)
    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }
}

