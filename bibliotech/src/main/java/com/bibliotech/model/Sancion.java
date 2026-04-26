package com.bibliotech.model;

import java.time.LocalDate;

public class Sancion {

    private final int id;
    private final int dniSocio;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final String motivo;

    public Sancion(int id, int dniSocio, LocalDate fechaInicio, LocalDate fechaFin, String motivo) {
        this.id = id;
        this.dniSocio = dniSocio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo;
    }

    public int getId() { return id; }
    public int getDniSocio() { return dniSocio; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public String getMotivo() { return motivo; }

    public boolean estaActivaEn(LocalDate fecha) {
        if (fecha == null) {
            return false;
        }
        return !fecha.isBefore(fechaInicio) && fecha.isBefore(fechaFin);
    }
}
