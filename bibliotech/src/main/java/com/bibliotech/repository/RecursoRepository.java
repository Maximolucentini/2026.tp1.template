package com.bibliotech.repository;

import com.bibliotech.model.Categoria;
import com.bibliotech.model.Recurso;
import java.util.List;
import java.util.Optional;

public interface RecursoRepository<T extends Recurso> extends Repository<T, String> {
    Optional<T> buscarPorIsbn(String isbn);
    List<T> buscarPorTitulo(String titulo);
    List<T> buscarPorAutor(String autor);
    List<T> buscarPorCategoria(Categoria categoria);
}
