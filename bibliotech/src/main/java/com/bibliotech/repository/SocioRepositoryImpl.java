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
    public Optional<Socio> buscarPorDni(int dni) {
        for (Socio socio : socios) {
            if (socio.getDni() == dni) {
                return Optional.of(socio);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Socio> buscarTodos() {
        return new ArrayList<>(socios);
    }
}