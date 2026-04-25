package com.bibliotech.repository;


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
    public Optional<T> buscarPorIsbn(String isbn) {
        for (T recurso : recursos) {
            if (recurso.isbn().equals(isbn)) {
                return Optional.of(recurso);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<T> buscarTodos() {
        return new ArrayList<>(recursos);
    }
}
