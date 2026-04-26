package com.bibliotech.service;

import com.bibliotech.exception.LimitePrestamosExcedidoException;
import com.bibliotech.exception.PrestamoInvalidoException;
import com.bibliotech.exception.PrestamoNoEncontradoException;
import com.bibliotech.exception.RecursoNoDisponibleException;
import com.bibliotech.exception.RecursoNoEncontradoException;
import com.bibliotech.exception.SocioNoEncontradoException;
import com.bibliotech.model.Prestamo;
import com.bibliotech.model.Recurso;
import com.bibliotech.model.Socio;
import com.bibliotech.repository.PrestamoRepository;
import com.bibliotech.repository.RecursoRepository;
import com.bibliotech.repository.SocioRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PrestamoService {

    private static final int DIAS_PRESTAMO = 15;

    private final PrestamoRepository prestamoRepository;
    private final RecursoRepository<Recurso> recursoRepository;
    private final SocioRepository socioRepository;

    public PrestamoService(
            PrestamoRepository prestamoRepository,
            RecursoRepository<Recurso> recursoRepository,
            SocioRepository socioRepository) {
        this.prestamoRepository = prestamoRepository;
        this.recursoRepository = recursoRepository;
        this.socioRepository = socioRepository;
    }

    public Prestamo registrarPrestamo(String isbnRecurso, int dniSocio)
            throws PrestamoInvalidoException,
            RecursoNoEncontradoException,
            SocioNoEncontradoException,
            RecursoNoDisponibleException,
            LimitePrestamosExcedidoException {

        if (isbnRecurso == null || isbnRecurso.isBlank()) {
            throw new PrestamoInvalidoException("El ISBN del recurso no puede estar vacio");
        }
        if (dniSocio <= 0) {
            throw new PrestamoInvalidoException("El DNI del socio debe ser mayor a 0");
        }

        Recurso recurso = recursoRepository.buscarPorIsbn(isbnRecurso)
                .orElseThrow(() -> new RecursoNoEncontradoException(isbnRecurso));

        Socio socio = socioRepository.buscarPorDni(dniSocio)
                .orElseThrow(() -> new SocioNoEncontradoException(dniSocio));

        if (!recurso.estaDisponible()) {
            throw new RecursoNoDisponibleException("El recurso con ISBN " + isbnRecurso + " no esta disponible");
        }

        if (prestamoRepository.buscarActivoPorIsbnRecurso(isbnRecurso).isPresent()) {
            throw new RecursoNoDisponibleException("El recurso con ISBN " + isbnRecurso + " ya esta prestado");
        }

        List<Prestamo> prestamosActivos = prestamoRepository.buscarActivosPorDniSocio(dniSocio);
        if (prestamosActivos.size() >= socio.getTipo().getMaximoLibros()) {
            throw new LimitePrestamosExcedidoException(
                    "El socio con DNI " + dniSocio + " alcanzo el límite de prestamos ("
                            + socio.getTipo().getMaximoLibros() + ")");
        }

        int nuevoId = prestamoRepository.buscarTodos().size() + 1;
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaVencimiento = fechaPrestamo.plusDays(DIAS_PRESTAMO);

        Prestamo prestamo = new Prestamo(nuevoId, isbnRecurso, dniSocio, fechaPrestamo, fechaVencimiento, true);
        prestamoRepository.guardar(prestamo);

        return prestamo;
    }

    public Prestamo registrarDevolucion(String isbnRecurso, LocalDate fechaDevolucion)
            throws PrestamoInvalidoException,
            PrestamoNoEncontradoException {

        if (isbnRecurso == null || isbnRecurso.isBlank()) {
            throw new PrestamoInvalidoException("El ISBN del recurso no puede estar vacio");
        }
        if (fechaDevolucion == null) {
            throw new PrestamoInvalidoException("La fecha de devolucion no puede ser null");
        }

        Prestamo prestamo = prestamoRepository.buscarActivoPorIsbnRecurso(isbnRecurso)
                .orElseThrow(() -> new PrestamoNoEncontradoException(
                        "No existe prestamo activo para el ISBN: " + isbnRecurso));

        if (fechaDevolucion.isBefore(prestamo.getFechaPrestamo())) {
            throw new PrestamoInvalidoException("La fecha de devolucion no puede ser anterior a la fecha de prestamo");
        }

        int diasRetraso = 0;
        if (fechaDevolucion.isAfter(prestamo.getFechaVencimiento())) {
            diasRetraso = (int) ChronoUnit.DAYS.between(prestamo.getFechaVencimiento(), fechaDevolucion);
        }

        prestamo.registrarDevolucion(fechaDevolucion, diasRetraso);

        return prestamo;
    }

    public Prestamo registrarDevolucion(String isbnRecurso)
            throws PrestamoInvalidoException,
            PrestamoNoEncontradoException {
        return registrarDevolucion(isbnRecurso, LocalDate.now());
    }
}