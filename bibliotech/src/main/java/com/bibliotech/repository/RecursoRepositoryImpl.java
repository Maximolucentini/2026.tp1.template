package com.bibliotech.repository;

import com.bibliotech.model.Categoria;
import com.bibliotech.model.Recurso;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecursoRepositoryImpl<T extends Recurso> implements RecursoRepository<T> {

    private final List<T> recursos = new ArrayList<>();

    @Override
    public void guardar(T recurso) {
        recursos.add(recurso);
    }

    @Override
    public Optional<T> buscarPorId(String isbn) {
        return recursos.stream()
                .filter(recurso -> recurso.isbn().equals(isbn))
                .findFirst();
    }

    @Override
    public Optional<T> buscarPorIsbn(String isbn) {
        return buscarPorId(isbn);
    }

    @Override
    public List<T> buscarTodos() {
        return new ArrayList<>(recursos);
    }

    @Override
    public List<T> buscarPorTitulo(String titulo) {
        return recursos.stream()
                .filter(recurso -> recurso.titulo().toLowerCase().contains(titulo.toLowerCase()))
                .toList();
    }

    @Override
    public List<T> buscarPorAutor(String autor) {
        return recursos.stream()
                .filter(recurso -> recurso.autor().toLowerCase().contains(autor.toLowerCase()))
                .toList();
    }

    @Override
    public List<T> buscarPorCategoria(Categoria categoria) {
        return recursos.stream()
                .filter(recurso -> recurso.categoria() == categoria)
                .toList();
    }
}