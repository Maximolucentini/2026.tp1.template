package com.bibliotech.repository;

import com.bibliotech.model.Prestamo;
import java.util.List;
import java.util.Optional;

public interface PrestamoRepository extends Repository<Prestamo, Integer> {
    List<Prestamo> buscarActivosPorDniSocio(int dniSocio);
    Optional<Prestamo> buscarActivoPorIsbnRecurso(String isbnRecurso);
    List<Prestamo> buscarHistorialPorDniSocio(int dniSocio);
    List<Prestamo> buscarHistorialPorIsbnRecurso(String isbnRecurso);
}