package com.bibliotech.service;

import com.bibliotech.exception.RecursoInvalidoException;
import com.bibliotech.exception.RecursoNoEncontradoException;
import com.bibliotech.model.Recurso;
import com.bibliotech.model.Categoria;
import com.bibliotech.repository.RecursoRepository;
import java.util.List;
import java.util.Optional;

public class RecursoService<T extends Recurso> {

    private final RecursoRepository<T> repository;

    public RecursoService(RecursoRepository<T> repository) {
        this.repository = repository;
    }


    public void registrar(T recurso) throws RecursoInvalidoException {
        if (recurso == null) {
            throw new RecursoInvalidoException("El recurso no puede ser null");
        }
        if (recurso.isbn() == null || recurso.isbn().isBlank()) {
            throw new RecursoInvalidoException("El ISBN no puede estar vacío");
        }
        if (recurso.titulo() == null || recurso.titulo().isBlank()) {
            throw new RecursoInvalidoException("El título no puede estar vacío");
        }
        if (recurso.autor() == null || recurso.autor().isBlank()) {
            throw new RecursoInvalidoException("El autor no puede estar vacío");
        }
        if (recurso.anio() <= 0) {
            throw new RecursoInvalidoException("El año debe ser mayor a 0");
        }
        if (recurso.categoria() == null) {
            throw new RecursoInvalidoException("La categoría no puede ser null");
        }

        if (repository.buscarPorIsbn(recurso.isbn()).isPresent()) {
            throw new RecursoInvalidoException("Ya existe un recurso con ISBN: " + recurso.isbn());
        }
        repository.guardar(recurso);
    }

    public T buscarPorIsbn(String isbn) throws RecursoNoEncontradoException {
        Optional<T> resultado = repository.buscarPorIsbn(isbn);
        if (resultado.isPresent()) {
            return resultado.get();
        }
        throw new RecursoNoEncontradoException(isbn);
    }

    public List<T> buscarTodos() {
        return repository.buscarTodos();
    }

    public List<T> buscarPorTitulo(String titulo) {
        return repository.buscarPorTitulo(titulo);
    }

    public List<T> buscarPorAutor(String autor) {
        return repository.buscarPorAutor(autor);
    }

    public List<T> buscarPorCategoria(Categoria categoria) {
        return repository.buscarPorCategoria(categoria);
    }
}
