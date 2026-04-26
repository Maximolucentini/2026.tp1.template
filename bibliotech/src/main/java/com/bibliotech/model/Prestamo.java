package com.bibliotech.model;

import java.time.LocalDate;

public class Prestamo {

    private final int id;
    private final String isbnRecurso;
    private final int dniSocio;
    private final LocalDate fechaPrestamo;
    private final boolean activo;

    public Prestamo(int id, String isbnRecurso, int dniSocio, LocalDate fechaPrestamo, boolean activo) {
        this.id = id;
        this.isbnRecurso = isbnRecurso;
        this.dniSocio = dniSocio;
        this.fechaPrestamo = fechaPrestamo;
        this.activo = activo;
    }

    public int getId() { return id; }
    public String getIsbnRecurso() { return isbnRecurso; }
    public int getDniSocio() { return dniSocio; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public boolean estaActivo() { return activo; }
}
