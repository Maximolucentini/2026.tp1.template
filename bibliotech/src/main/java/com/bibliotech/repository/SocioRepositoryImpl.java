package com.bibliotech.repository;

import com.bibliotech.model.Socio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SocioRepositoryImpl implements SocioRepository {

    private final List<Socio> socios = new ArrayList<>();

    @Override
    public void guardar(Socio socio) {
        socios.add(socio);
    }

    @Override
    public Optional<Socio> buscarPorId(Integer dni) {
        if (dni == null) {
            return Optional.empty();
        }
        return socios.stream()
                .filter(socio -> socio.getDni() == dni)
                .findFirst();
    }

    @Override
    public Optional<Socio> buscarPorDni(int dni) {
        return buscarPorId(dni);
    }

    @Override
    public List<Socio> buscarTodos() {
        return new ArrayList<>(socios);
    }
}