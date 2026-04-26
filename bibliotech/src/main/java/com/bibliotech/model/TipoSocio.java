package com.bibliotech.model;

public enum TipoSocio {
    ESTUDIANTE(3),
    DOCENTE(5);

    private final int maximoLibros;

    TipoSocio(int maximoLibros) {
        this.maximoLibros = maximoLibros;
    }

    public int getMaximoLibros() {
        return maximoLibros;
    }
}