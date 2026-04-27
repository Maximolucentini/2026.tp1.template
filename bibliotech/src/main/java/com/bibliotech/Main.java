package com.bibliotech;

import com.bibliotech.exception.SocioSancionadoException;
import com.bibliotech.model.*;
import com.bibliotech.repository.*;
import com.bibliotech.service.*;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        RecursoRepository<Recurso> recursoRepository  = new RecursoRepositoryImpl<>();
        SocioRepository            socioRepository    = new SocioRepositoryImpl();
        PrestamoRepository         prestamoRepository = new PrestamoRepositoryImpl();
        SancionRepository          sancionRepository  = new SancionRepositoryImpl();

        RecursoService<Recurso> recursoService  = new RecursoService<>(recursoRepository);
        SocioService            socioService    = new SocioService(socioRepository);
        SancionService          sancionService  = new SancionService(sancionRepository);
        PrestamoService         prestamoService = new PrestamoService(
                prestamoRepository, recursoRepository, socioRepository, sancionService);
        PersistenciaCsvService  persistenciaService = new PersistenciaCsvService(
                recursoRepository, socioRepository);

        try {

            System.out.println("=== Alta de recursos ===");

            Recurso libro = new LibroFisico(
                    "978-0-13-468599-1",
                    "Clean Code",
                    "Robert C. Martin",
                    2008,
                    Categoria.TECNOLOGIA,
                    true
            );
            Recurso ebook = new Ebook(
                    "978-0-13-235088-4",
                    "The Pragmatic Programmer",
                    "David Thomas",
                    2019,
                    Categoria.TECNOLOGIA,
                    "PDF"
            );

            recursoService.registrar(libro);
            recursoService.registrar(ebook);
            System.out.println("Recursos registrados: " + recursoService.buscarTodos().size());

            System.out.println("\n=== Busqueda avanzada ===");

            List<Recurso> porTitulo = recursoService.buscarPorTitulo("clean");
            System.out.println("Por titulo 'clean': " + porTitulo.size() + " resultado/s");

            List<Recurso> porAutor = recursoService.buscarPorAutor("thomas");
            System.out.println("Por autor 'thomas': " + porAutor.size() + " resultado/s");

            List<Recurso> porCategoria = recursoService.buscarPorCategoria(Categoria.TECNOLOGIA);
            System.out.println("Por categoría TECNOLOGIA: " + porCategoria.size() + " resultado/s");

            System.out.println("\n=== Alta de socios ===");

            Socio estudiante = new Socio(12345678, "Ana Garcia", "ana@email.com", TipoSocio.ESTUDIANTE);
            Socio docente    = new Socio(87654321, "Carlos Lopez", "carlos@email.com", TipoSocio.DOCENTE);

            socioService.registrar(estudiante);
            socioService.registrar(docente);
            System.out.println("Socios registrados: " + socioService.buscarTodos().size());

            System.out.println("\n=== Registro de prestamo ===");

            Prestamo prestamo = prestamoService.registrarPrestamo("978-0-13-468599-1", 12345678);
            System.out.println("Prestamo registrado:");
            System.out.println("  ID: " + prestamo.getId());
            System.out.println("  ISBN: " + prestamo.getIsbnRecurso());
            System.out.println("  DNI socio: " + prestamo.getDniSocio());
            System.out.println("  Fecha prestamo: " + prestamo.getFechaPrestamo());
            System.out.println("  Fecha vencimiento: " + prestamo.getFechaVencimiento());
            System.out.println("  Activo: " + prestamo.estaActivo());

            System.out.println("\n=== Devolucion con retraso ===");

            LocalDate fechaDevolucionTardia = prestamo.getFechaVencimiento().plusDays(3);
            Prestamo devuelto = prestamoService.registrarDevolucion(
                    "978-0-13-468599-1", fechaDevolucionTardia);

            System.out.println("Devolucion registrada:");
            System.out.println("  Fecha devolucion: " + devuelto.getFechaDevolucion());
            System.out.println("  Días de retraso: " + devuelto.getDiasRetraso());
            System.out.println("  Activo: " + devuelto.estaActivo());

            System.out.println("\n=== Intento de prestamo con sancion activa ===");

            sancionService.aplicarSancionPorRetraso(
                    estudiante.getDni(),
                    LocalDate.now(),
                    3
            );

            try {
                prestamoService.registrarPrestamo("978-0-13-235088-4", 12345678);
            } catch (SocioSancionadoException e) {
                System.out.println("Sanción detectada correctamente: " + e.getMessage());
            }

            System.out.println("\n=== Historial ===");

            List<Prestamo> historialCompleto = prestamoService.obtenerHistorialCompleto();
            System.out.println("Historial completo: " + historialCompleto.size() + " prestamo/s");

            List<Prestamo> historialSocio = prestamoService.obtenerHistorialPorSocio(12345678);
            System.out.println("Historial socio DNI 12345678: " + historialSocio.size() + " prestamo/s");

            List<Prestamo> historialRecurso = prestamoService.obtenerHistorialPorRecurso("978-0-13-468599-1");
            System.out.println("Historial ISBN 978-0-13-468599-1: " + historialRecurso.size() + " prestamo/s");

            System.out.println("\n=== Persistencia CSV ===");

            Path carpetaDatos = Path.of("data");
            persistenciaService.guardarTodo(carpetaDatos);
            System.out.println("Datos guardados en carpeta: " + carpetaDatos.toAbsolutePath());

            persistenciaService.cargarTodo(carpetaDatos);
            System.out.println("Datos cargados (duplicados ignorados)");
            System.out.println("Recursos en memoria: " + recursoService.buscarTodos().size());
            System.out.println("Socios en memoria: " + socioService.buscarTodos().size());

            System.out.println("\n=== Sistema BiblioTech ejecutado correctamente ===");

        } catch (Exception e) {
            System.out.println("Error durante la ejecucion: " + e.getMessage());
        }
    }
}
