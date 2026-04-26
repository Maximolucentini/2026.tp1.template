package com.bibliotech.model;

public class Socio {

    private final int dni;
    private final String nombre;
    private final String email;
    private final TipoSocio tipo;

    public Socio(int dni, String nombre, String email, TipoSocio tipo) {
        this.dni = dni;
        this.nombre = nombre;
        this.email = email;
        this.tipo = tipo;
    }

    public int getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public TipoSocio getTipo() { return tipo; }
}


