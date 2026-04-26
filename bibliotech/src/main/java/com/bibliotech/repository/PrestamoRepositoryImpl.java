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
                .filter(prestamo -> prestamo.getDniSocio() == dniSocio && prestamo.estaActivo())
                .toList();
    }

    @Override
    public Optional<Prestamo> buscarActivoPorIsbnRecurso(String isbnRecurso) {
        if (isbnRecurso == null) {
            return Optional.empty();
        }
        return prestamos.stream()
                .filter(prestamo -> prestamo.getIsbnRecurso().equals(isbnRecurso) && prestamo.estaActivo())
                .findFirst();
    }
}
