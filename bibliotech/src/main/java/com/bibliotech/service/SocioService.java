package com.bibliotech.service;

import com.bibliotech.exception.SocioInvalidoException;
import com.bibliotech.exception.SocioNoEncontradoException;
import com.bibliotech.model.Socio;
import com.bibliotech.repository.SocioRepository;
import java.util.List;
import java.util.Optional;

public class SocioService {

    private final SocioRepository repository;

    public SocioService(SocioRepository repository) {
        this.repository = repository;
    }

    public void registrar(Socio socio) throws SocioInvalidoException {
        if (socio == null) {
            throw new SocioInvalidoException("El socio no puede ser null");
        }
        if (socio.getNombre() == null || socio.getNombre().isBlank()) {
            throw new SocioInvalidoException("El nombre no puede estar vacío");
        }
        if (socio.getDni() <= 0) {
            throw new SocioInvalidoException("El DNI debe ser mayor a 0");
        }
        if (socio.getEmail() == null || socio.getEmail().isBlank()) {
            throw new SocioInvalidoException("El email no puede estar vacío");
        }
        if (!emailValido(socio.getEmail())) {
            throw new SocioInvalidoException("El email no tiene un formato válido");
        }
        if (repository.buscarPorDni(socio.getDni()).isPresent()) {
            throw new SocioInvalidoException("Ya existe un socio con DNI: " + socio.getDni());
        }
        repository.guardar(socio);
    }

    private boolean emailValido(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public Socio buscarPorDni(int dni) throws SocioNoEncontradoException {
        Optional<Socio> resultado = repository.buscarPorDni(dni);
        if (resultado.isPresent()) {
            return resultado.get();
        }
        throw new SocioNoEncontradoException(dni);
    }

    public List<Socio> buscarTodos() {
        return repository.buscarTodos();
    }
}
