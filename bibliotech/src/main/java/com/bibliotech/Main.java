package com.bibliotech;

import com.bibliotech.exception.SocioSancionadoException;
import com.bibliotech.model.*;
import com.bibliotech.repository.*;
import com.bibliotech.service.*;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static RecursoRepository<Recurso> recursoRepository;
    private static SocioRepository            socioRepository;
    private static PrestamoRepository         prestamoRepository;
    private static SancionRepository          sancionRepository;

    private static RecursoService<Recurso> recursoService;
    private static SocioService            socioService;
    private static SancionService          sancionService;
    private static PrestamoService         prestamoService;
    private static PersistenciaCsvService  persistenciaService;

    public static void main(String[] args) {

        recursoRepository  = new RecursoRepositoryImpl<>();
        socioRepository    = new SocioRepositoryImpl();
        prestamoRepository = new PrestamoRepositoryImpl();
        sancionRepository  = new SancionRepositoryImpl();

        recursoService      = new RecursoService<>(recursoRepository);
        socioService        = new SocioService(socioRepository);
        sancionService      = new SancionService(sancionRepository);
        prestamoService     = new PrestamoService(
                prestamoRepository, recursoRepository, socioRepository, sancionService);
        persistenciaService = new PersistenciaCsvService(recursoRepository, socioRepository);

        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.");
                continue;
            }

            switch (opcion) {
                case 1  -> ejecutarDemoCompleta();
                case 2  -> registrarLibroFisico();
                case 3  -> registrarEbook();
                case 4  -> registrarSocio();
                case 5  -> buscarRecursos();
                case 6  -> registrarPrestamo();
                case 7  -> registrarDevolucion();
                case 8  -> mostrarHistorial();
                case 9  -> guardarDatosCsv();
                case 10 -> cargarDatosCsv();
                case 0  -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción no reconocida.");
            }
        }
    }


    private static void mostrarMenu() {
        System.out.println("\n=== BiblioTech ===");
        System.out.println("1. Ejecutar demo completa");
        System.out.println("2. Registrar libro fisico");
        System.out.println("3. Registrar e-book");
        System.out.println("4. Registrar socio");
        System.out.println("5. Buscar recursos");
        System.out.println("6. Registrar prestamo");
        System.out.println("7. Registrar devolucion");
        System.out.println("8. Ver historial");
        System.out.println("9. Guardar datos CSV");
        System.out.println("10. Cargar datos CSV");
        System.out.println("0. Salir");
        System.out.print("Opción: ");
    }



    private static void ejecutarDemoCompleta() {
        try {
            System.out.println("\n=== Alta de recursos ===");
            Recurso libro = new LibroFisico(
                    "978-0-13-468599-1", "Clean Code", "Robert C. Martin",
                    2008, Categoria.TECNOLOGIA, true);
            Recurso ebook = new Ebook(
                    "978-0-13-235088-4", "The Pragmatic Programmer", "David Thomas",
                    2019, Categoria.TECNOLOGIA, "PDF");
            recursoService.registrar(libro);
            recursoService.registrar(ebook);
            System.out.println("Recursos registrados: " + recursoService.buscarTodos().size());

            System.out.println("\n=== Busqueda avanzada ===");
            System.out.println("Por titulo 'clean': " + recursoService.buscarPorTitulo("clean").size() + " resultado/s");
            System.out.println("Por autor 'thomas': " + recursoService.buscarPorAutor("thomas").size() + " resultado/s");
            System.out.println("Por categoría TECNOLOGIA: " + recursoService.buscarPorCategoria(Categoria.TECNOLOGIA).size() + " resultado/s");

            System.out.println("\n=== Alta de socios ===");
            Socio estudiante = new Socio(12345678, "Ana García", "ana@email.com", TipoSocio.ESTUDIANTE);
            Socio docente    = new Socio(87654321, "Carlos López", "carlos@email.com", TipoSocio.DOCENTE);
            socioService.registrar(estudiante);
            socioService.registrar(docente);
            System.out.println("Socios registrados: " + socioService.buscarTodos().size());

            System.out.println("\n=== Registro de préstamo ===");
            Prestamo prestamo = prestamoService.registrarPrestamo("978-0-13-468599-1", 12345678);
            System.out.println("Prestamo ID " + prestamo.getId() + " registrado. Vence: " + prestamo.getFechaVencimiento());

            System.out.println("\n=== Devolución con retraso ===");
            Prestamo devuelto = prestamoService.registrarDevolucion(
                    "978-0-13-468599-1", prestamo.getFechaVencimiento().plusDays(3));
            System.out.println("Fecha devolucion: " + devuelto.getFechaDevolucion());
            System.out.println("Días de retraso: " + devuelto.getDiasRetraso());
            System.out.println("Activo: " + devuelto.estaActivo());

            System.out.println("\n=== Intento de prestamo con sancion activa ===");
            sancionService.aplicarSancionPorRetraso(estudiante.getDni(), LocalDate.now(), 3);
            try {
                prestamoService.registrarPrestamo("978-0-13-235088-4", 12345678);
            } catch (SocioSancionadoException e) {
                System.out.println("Sancion detectada: " + e.getMessage());
            }

            System.out.println("\n=== Historial ===");
            System.out.println("Historial completo: " + prestamoService.obtenerHistorialCompleto().size() + " prestamo/s");
            System.out.println("Historial socio 12345678: " + prestamoService.obtenerHistorialPorSocio(12345678).size() + " prestamo/s");
            System.out.println("Historial ISBN 978-0-13-468599-1: " + prestamoService.obtenerHistorialPorRecurso("978-0-13-468599-1").size() + " prestamo/s");

            System.out.println("\n=== Persistencia CSV ===");
            Path carpeta = Path.of("data");
            persistenciaService.guardarTodo(carpeta);
            System.out.println("Datos guardados en: " + carpeta.toAbsolutePath());
            persistenciaService.cargarTodo(carpeta);
            System.out.println("Carga completada (duplicados ignorados)");

            System.out.println("\n=== Demo completada correctamente ===");

        } catch (Exception e) {
            System.out.println("Error en la demo: " + e.getMessage());
        }
    }


    private static void registrarLibroFisico() {
        try {
            System.out.print("ISBN: ");
            String isbn = scanner.nextLine().trim();
            System.out.print("Titulo: ");
            String titulo = scanner.nextLine().trim();
            System.out.print("Autor: ");
            String autor = scanner.nextLine().trim();
            System.out.print("Año: ");
            int anio = Integer.parseInt(scanner.nextLine().trim());
            System.out.println("Categorias: " + java.util.Arrays.toString(Categoria.values()));
            System.out.print("Categoria: ");
            Categoria categoria = Categoria.valueOf(scanner.nextLine().trim().toUpperCase());

            recursoService.registrar(new LibroFisico(isbn, titulo, autor, anio, categoria, true));
            System.out.println("Libro fisico registrado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al registrar libro: " + e.getMessage());
        }
    }


    private static void registrarEbook() {
        try {
            System.out.print("ISBN: ");
            String isbn = scanner.nextLine().trim();
            System.out.print("Titulo: ");
            String titulo = scanner.nextLine().trim();
            System.out.print("Autor: ");
            String autor = scanner.nextLine().trim();
            System.out.print("Año: ");
            int anio = Integer.parseInt(scanner.nextLine().trim());
            System.out.println("Categorias: " + java.util.Arrays.toString(Categoria.values()));
            System.out.print("Categoría: ");
            Categoria categoria = Categoria.valueOf(scanner.nextLine().trim().toUpperCase());
            System.out.print("Formato (PDF, EPUB, MOBI): ");
            String formato = scanner.nextLine().trim();

            recursoService.registrar(new Ebook(isbn, titulo, autor, anio, categoria, formato));
            System.out.println("Ebook registrado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al registrar ebook: " + e.getMessage());
        }
    }



    private static void registrarSocio() {
        try {
            System.out.print("DNI: ");
            int dni = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            System.out.println("Tipo (ESTUDIANTE / DOCENTE): ");
            TipoSocio tipo = TipoSocio.valueOf(scanner.nextLine().trim().toUpperCase());

            socioService.registrar(new Socio(dni, nombre, email, tipo));
            System.out.println("Socio registrado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al registrar socio: " + e.getMessage());
        }
    }



    private static void buscarRecursos() {
        try {
            System.out.println("Buscar por: 1. Título  2. Autor  3. Categoría");
            System.out.print("Opción: ");
            String opcion = scanner.nextLine().trim();

            List<Recurso> resultados;

            switch (opcion) {
                case "1" -> {
                    System.out.print("Título: ");
                    resultados = recursoService.buscarPorTitulo(scanner.nextLine().trim());
                }
                case "2" -> {
                    System.out.print("Autor: ");
                    resultados = recursoService.buscarPorAutor(scanner.nextLine().trim());
                }
                case "3" -> {
                    System.out.println("Categorias: " + java.util.Arrays.toString(Categoria.values()));
                    System.out.print("Categoria: ");
                    Categoria cat = Categoria.valueOf(scanner.nextLine().trim().toUpperCase());
                    resultados = recursoService.buscarPorCategoria(cat);
                }
                default -> {
                    System.out.println("Opcion invalida.");
                    return;
                }
            }

            if (resultados.isEmpty()) {
                System.out.println("No se encontraron recursos.");
            } else {
                resultados.forEach(r -> System.out.println("  - [" + r.isbn() + "] " + r.titulo() + " — " + r.autor()));
            }

        } catch (Exception e) {
            System.out.println("Error en la busqueda: " + e.getMessage());
        }
    }


    private static void registrarPrestamo() {
        try {
            System.out.print("ISBN del recurso: ");
            String isbn = scanner.nextLine().trim();
            System.out.print("DNI del socio: ");
            int dni = Integer.parseInt(scanner.nextLine().trim());

            Prestamo prestamo = prestamoService.registrarPrestamo(isbn, dni);
            System.out.println("Prestamo registrado. ID: " + prestamo.getId() + " — Vence: " + prestamo.getFechaVencimiento());

        } catch (Exception e) {
            System.out.println("Error al registrar prestamo: " + e.getMessage());
        }
    }


    private static void registrarDevolucion() {
        try {
            System.out.print("ISBN del recurso a devolver: ");
            String isbn = scanner.nextLine().trim();

            Prestamo devuelto = prestamoService.registrarDevolucion(isbn);
            System.out.println("Devolucion registrada.");
            System.out.println("  Fecha: " + devuelto.getFechaDevolucion());
            System.out.println("  Días de retraso: " + devuelto.getDiasRetraso());

        } catch (Exception e) {
            System.out.println("Error al registrar devolucion: " + e.getMessage());
        }
    }


    private static void mostrarHistorial() {
        try {
            List<Prestamo> historial = prestamoService.obtenerHistorialCompleto();
            if (historial.isEmpty()) {
                System.out.println("No hay préstamos registrados.");
                return;
            }
            System.out.println("\n=== Historial completo ===");
            for (Prestamo p : historial) {
                System.out.println("  ID: " + p.getId()
                        + " | ISBN: " + p.getIsbnRecurso()
                        + " | DNI: " + p.getDniSocio()
                        + " | Prestamo: " + p.getFechaPrestamo()
                        + " | Activo: " + p.estaActivo()
                        + (p.getDiasRetraso() > 0 ? " | Retraso: " + p.getDiasRetraso() + " dias" : ""));
            }
        } catch (Exception e) {
            System.out.println("Error al mostrar historial: " + e.getMessage());
        }
    }


    private static void guardarDatosCsv() {
        try {
            Path carpeta = Path.of("data");
            persistenciaService.guardarTodo(carpeta);
            System.out.println("Datos guardados correctamente en: " + carpeta.toAbsolutePath());
        } catch (Exception e) {
            System.out.println("Error al guardar datos: " + e.getMessage());
        }
    }


    private static void cargarDatosCsv() {
        try {
            Path carpeta = Path.of("data");
            persistenciaService.cargarTodo(carpeta);
            System.out.println("Datos cargados correctamente desde: " + carpeta.toAbsolutePath());
        } catch (Exception e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
        }
    }
}