package com.bibliotech.repository;

import com.bibliotech.model.Socio;
import java.util.List;
import java.util.Optional;


public interface SocioRepository {
    void guardar(Socio socio);
    Optional<Socio> buscarPorDni(int dni);
    List<Socio> buscarTodos();
}
