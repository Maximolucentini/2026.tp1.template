package com.bibliotech.service;

import com.bibliotech.model.Sancion;
import com.bibliotech.repository.SancionRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SancionService {

    private final SancionRepository sancionRepository;

    public SancionService(SancionRepository sancionRepository) {
        this.sancionRepository = sancionRepository;
    }

    public Optional<Sancion> aplicarSancionPorRetraso(int dniSocio, LocalDate fechaDevolucion, int diasRetraso) {
        if (dniSocio <= 0 || fechaDevolucion == null || diasRetraso <= 0) {
            return Optional.empty();
        }

        int nuevoId = sancionRepository.buscarTodos().size() + 1;
        LocalDate fechaInicio = fechaDevolucion;
        LocalDate fechaFin = fechaDevolucion.plusDays(diasRetraso);
        String motivo = "Devolucion tardia de " + diasRetraso + " dias";

        Sancion sancion = new Sancion(nuevoId, dniSocio, fechaInicio, fechaFin, motivo);
        sancionRepository.guardar(sancion);

        return Optional.of(sancion);
    }

    public Optional<Sancion> buscarSancionActiva(int dniSocio, LocalDate fecha) {
        return sancionRepository.buscarActivaPorDniSocio(dniSocio, fecha);
    }

    public List<Sancion> obtenerSancionesPorSocio(int dniSocio) {
        return sancionRepository.buscarPorDniSocio(dniSocio);
    }
}
