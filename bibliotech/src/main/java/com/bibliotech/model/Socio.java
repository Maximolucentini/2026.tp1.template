package com.bibliotech.model;

public class Socio {

    private final int dni;
    private final String nombre;
    private final String email;

    public Socio(int dni, String nombre, String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.email = email;
    }

    public int getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
}

