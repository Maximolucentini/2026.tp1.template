package com.bibliotech.model;

import java.time.LocalDate;

public class Prestamo {

    private final int id;
    private final String isbnRecurso;
    private final int dniSocio;
    private final LocalDate fechaPrestamo;
    private final LocalDate fechaVencimiento;
    private LocalDate fechaDevolucion;
    private int diasRetraso;
    private boolean activo;

    public Prestamo(int id, String isbnRecurso, int dniSocio,
                    LocalDate fechaPrestamo, LocalDate fechaVencimiento, boolean activo) {
        this.id = id;
        this.isbnRecurso = isbnRecurso;
        this.dniSocio = dniSocio;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaVencimiento = fechaVencimiento;
        this.activo = activo;
        this.diasRetraso = 0;
    }

    public int getId() { return id; }
    public String getIsbnRecurso() { return isbnRecurso; }
    public int getDniSocio() { return dniSocio; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public int getDiasRetraso() { return diasRetraso; }
    public boolean estaActivo() { return activo; }


    public void registrarDevolucion(LocalDate fechaDevolucion, int diasRetraso) {
        this.fechaDevolucion = fechaDevolucion;
        this.diasRetraso = diasRetraso;
        this.activo = false;
    }
}
