package com.bibliotech.service;

import com.bibliotech.exception.PersistenciaException;
import com.bibliotech.model.Categoria;
import com.bibliotech.model.Ebook;
import com.bibliotech.model.LibroFisico;
import com.bibliotech.model.Recurso;
import com.bibliotech.model.Socio;
import com.bibliotech.model.TipoSocio;
import com.bibliotech.repository.RecursoRepository;
import com.bibliotech.repository.SocioRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaCsvService {

    private static final String SEPARADOR = ";";

    private final RecursoRepository<Recurso> recursoRepository;
    private final SocioRepository socioRepository;

    public PersistenciaCsvService(
            RecursoRepository<Recurso> recursoRepository,
            SocioRepository socioRepository) {
        this.recursoRepository = recursoRepository;
        this.socioRepository = socioRepository;
    }


    public void guardarTodo(Path carpetaDatos) throws PersistenciaException {
        if (carpetaDatos == null) {
            throw new PersistenciaException("La carpeta de datos no puede ser null");
        }
        try {
            Files.createDirectories(carpetaDatos);
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo crear la carpeta de datos", e);
        }
        guardarRecursos(carpetaDatos.resolve("recursos.csv"));
        guardarSocios(carpetaDatos.resolve("socios.csv"));
    }


    public void cargarTodo(Path carpetaDatos) throws PersistenciaException {
        if (carpetaDatos == null) {
            throw new PersistenciaException("La carpeta de datos no puede ser null");
        }
        Path recursos = carpetaDatos.resolve("recursos.csv");
        Path socios   = carpetaDatos.resolve("socios.csv");

        if (Files.exists(recursos)) cargarRecursos(recursos);
        if (Files.exists(socios))   cargarSocios(socios);
    }

    // ── Recursos ──────────────────────────────────────────────────

    private void guardarRecursos(Path archivo) throws PersistenciaException {
        List<String> lineas = new ArrayList<>();
        lineas.add("tipo;isbn;titulo;autor;anio;categoria;disponible;formatoArchivo");

        for (Recurso r : recursoRepository.buscarTodos()) {
            if (r instanceof LibroFisico libro) {
                lineas.add(String.join(SEPARADOR,
                        "LIBRO_FISICO",
                        libro.isbn(),
                        libro.titulo(),
                        libro.autor(),
                        String.valueOf(libro.anio()),
                        libro.categoria().name(),
                        String.valueOf(libro.disponible()),
                        ""
                ));
            } else if (r instanceof Ebook ebook) {
                lineas.add(String.join(SEPARADOR,
                        "EBOOK",
                        ebook.isbn(),
                        ebook.titulo(),
                        ebook.autor(),
                        String.valueOf(ebook.anio()),
                        ebook.categoria().name(),
                        "",
                        ebook.formatoArchivo()
                ));
            }
        }

        escribirArchivo(archivo, lineas);
    }

    private void cargarRecursos(Path archivo) throws PersistenciaException {
        List<String> lineas = leerArchivo(archivo);

        for (int i = 1; i < lineas.size(); i++) {
            try {
                String[] cols = lineas.get(i).split(SEPARADOR, -1);
                validarColumnas(cols, 8, archivo.getFileName().toString(), i + 1);

                String tipo         = cols[0].trim();
                String isbn         = cols[1].trim();
                String titulo       = cols[2].trim();
                String autor        = cols[3].trim();
                int anio            = Integer.parseInt(cols[4].trim());
                Categoria categoria = Categoria.valueOf(cols[5].trim());

                if (recursoRepository.buscarPorIsbn(isbn).isPresent()) {
                    continue;
                }

                if (tipo.equals("LIBRO_FISICO")) {
                    boolean disponible = Boolean.parseBoolean(cols[6].trim());
                    recursoRepository.guardar((Recurso) new LibroFisico(isbn, titulo, autor, anio, categoria, disponible));
                } else if (tipo.equals("EBOOK")) {
                    String formato = cols[7].trim();
                    recursoRepository.guardar((Recurso) new Ebook(isbn, titulo, autor, anio, categoria, formato));
                }else {
                    throw new PersistenciaException("Tipo de recurso desconocido: " + tipo);
                }

            } catch (PersistenciaException e) {
                throw e;
            } catch (Exception e) {
                throw new PersistenciaException("Error al cargar recurso en linea " + (i + 1) + " de " + archivo.getFileName(), e);
            }
        }
    }


    private void guardarSocios(Path archivo) throws PersistenciaException {
        List<String> lineas = new ArrayList<>();
        lineas.add("dni;nombre;email;tipo");

        for (Socio s : socioRepository.buscarTodos()) {
            lineas.add(String.join(SEPARADOR,
                    String.valueOf(s.getDni()),
                    s.getNombre(),
                    s.getEmail(),
                    s.getTipo().name()
            ));
        }

        escribirArchivo(archivo, lineas);
    }

    private void cargarSocios(Path archivo) throws PersistenciaException {
        List<String> lineas = leerArchivo(archivo);

        for (int i = 1; i < lineas.size(); i++) {
            try {
                String[] cols = lineas.get(i).split(SEPARADOR, -1);
                validarColumnas(cols, 4, archivo.getFileName().toString(), i + 1);

                int dni        = Integer.parseInt(cols[0].trim());
                String nombre  = cols[1].trim();
                String email   = cols[2].trim();
                TipoSocio tipo = TipoSocio.valueOf(cols[3].trim());

                if (socioRepository.buscarPorDni(dni).isPresent()) {
                    continue;
                }

                socioRepository.guardar(new Socio(dni, nombre, email, tipo));

            } catch (PersistenciaException e) {
                throw e;
            } catch (Exception e) {
                throw new PersistenciaException("Error al cargar socio en línea " + (i + 1) + " de " + archivo.getFileName(), e);
            }
        }
    }


    private void escribirArchivo(Path archivo, List<String> lineas) throws PersistenciaException {
        try {
            Files.write(archivo, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo escribir el archivo: " + archivo.getFileName(), e);
        }
    }

    private List<String> leerArchivo(Path archivo) throws PersistenciaException {
        try {
            return Files.readAllLines(archivo, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo leer el archivo: " + archivo.getFileName(), e);
        }
    }

    private void validarColumnas(String[] cols, int esperadas, String archivo, int linea) throws PersistenciaException {
        if (cols.length < esperadas) {
            throw new PersistenciaException(
                    "Formato invalido en " + archivo + ", linea " + linea +
                            ": se esperaban " + esperadas + " columnas pero se encontraron " + cols.length);
        }
    }
}
