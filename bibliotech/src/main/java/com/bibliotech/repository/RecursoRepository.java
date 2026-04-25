package com.bibliotech.repository;

import com.bibliotech.model.Recurso;
import com.bibliotech.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface RecursoRepository<T extends Recurso> {
    void guardar(T recurso);

    Optional<T> buscarPorIsbn(String isbn);

    List<T> buscarTodos();

    List<T> buscarPorTitulo(String titulo);

    List<T> buscarPorAutor(String autor);

    List<T> buscarPorCategoria(Categoria categoria);
}
