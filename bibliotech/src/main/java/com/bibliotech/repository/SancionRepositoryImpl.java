package com.bibliotech.repository;

import com.bibliotech.model.Sancion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SancionRepositoryImpl implements SancionRepository {

    private final List<Sancion> sanciones = new ArrayList<>();

    @Override
    public void guardar(Sancion sancion) {
        sanciones.add(sancion);
    }

    @Override
    public Optional<Sancion> buscarPorId(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return sanciones.stream()
                .filter(s -> s.getId() == id)
                .findFirst();
    }

    @Override
    public List<Sancion> buscarTodos() {
        return new ArrayList<>(sanciones);
    }

    @Override
    public List<Sancion> buscarPorDniSocio(int dniSocio) {
        return sanciones.stream()
                .filter(s -> s.getDniSocio() == dniSocio)
                .toList();
    }

    @Override
    public Optional<Sancion> buscarActivaPorDniSocio(int dniSocio, LocalDate fecha) {
        if (fecha == null) {
            return Optional.empty();
        }
        return sanciones.stream()
                .filter(s -> s.getDniSocio() == dniSocio && s.estaActivaEn(fecha))
                .findFirst();
    }
}