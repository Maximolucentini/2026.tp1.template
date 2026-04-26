package com.bibliotech.repository;

import com.bibliotech.model.Sancion;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SancionRepository extends Repository<Sancion, Integer> {
    List<Sancion> buscarPorDniSocio(int dniSocio);
    Optional<Sancion> buscarActivaPorDniSocio(int dniSocio, LocalDate fecha);
}
