package com.bibliotech.repository;

import com.bibliotech.model.Recurso;
import java.util.List;
import java.util.Optional;

public interface RecursoRepository<T extends Recurso> {
    void guardar(T recurso);

    Optional<T> buscarPorIsbn(String isbn);

    List<T> buscarTodos();


}
