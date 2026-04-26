package com.bibliotech.repository;

import com.bibliotech.model.Socio;
import java.util.Optional;

public interface SocioRepository extends Repository<Socio, Integer> {
    Optional<Socio> buscarPorDni(int dni);
}