package com.bibliotech.model;

public record LibroFisico(
        String isbn,
        String titulo,
        String autor,
        int anio,
        Categoria categoria,
        boolean disponible
) implements Recurso {

    @Override
    public boolean estaDisponible() {
        return disponible;
    }
}
