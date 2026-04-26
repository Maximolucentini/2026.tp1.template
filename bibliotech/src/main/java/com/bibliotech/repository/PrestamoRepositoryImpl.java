package com.bibliotech.repository;

import com.bibliotech.model.Prestamo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrestamoRepositoryImpl implements PrestamoRepository {

    private final List<Prestamo> prestamos = new ArrayList<>();

    @Override
    public void guardar(Prestamo prestamo) {
        prestamos.add(prestamo);
    }

    @Override
    public Optional<Prestamo> buscarPorId(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return prestamos.stream()
                .filter(prestamo -> prestamo.getId() == id)
                .findFirst();
    }

    @Override
    public List<Prestamo> buscarTodos() {
        return new ArrayList<>(prestamos);
    }

    @Override
    public List<Prestamo> buscarActivosPorDniSocio(int dniSocio) {
        return prestamos.stream()
                .filter(p -> p.getDniSocio() == dniSocio && p.estaActivo())
                .toList();
    }

    @Override
    public Optional<Prestamo> buscarActivoPorIsbnRecurso(String isbnRecurso) {
        if (isbnRecurso == null) {
            return Optional.empty();
        }
        return prestamos.stream()
                .filter(p -> p.getIsbnRecurso().equals(isbnRecurso) && p.estaActivo())
                .findFirst();
    }

    @Override
    public List<Prestamo> buscarHistorialPorDniSocio(int dniSocio) {
        return prestamos.stream()
                .filter(p -> p.getDniSocio() == dniSocio)
                .toList();
    }

    @Override
    public List<Prestamo> buscarHistorialPorIsbnRecurso(String isbnRecurso) {
        if (isbnRecurso == null) {
            return new ArrayList<>();
        }

        return prestamos.stream()
                .filter(p -> isbnRecurso.equals(p.getIsbnRecurso()))
                .toList();

    }
}
