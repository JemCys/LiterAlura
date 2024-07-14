package com.rebollarclary.literalura.principal;

import com.rebollarclary.literalura.model.*;
import com.rebollarclary.literalura.repository.AutorRepository;
import com.rebollarclary.literalura.repository.LibroRepository;
import com.rebollarclary.literalura.service.ConsumoAPI;
import com.rebollarclary.literalura.service.ConvierteDatos;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos convierte = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    @Autowired
    public Principal (LibroRepository libroRepository, AutorRepository autorRepository){
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libros por título ~.~.~.~.~.~.~.~
                    2 - Mostrar libros registrados ~.~.~.~.~.~.~
                    3 - Mostrar autores registrados ~.~.~.~.~.~.~
                    4 - Autores vivos en determinado año ~.~.~.~
                    5 - Idiomas de los libros ~.~.~.~.~.~.~.~.~
                    6 - Top 10 libros más descargados ~.~.~.~.~.~.~
                    7 - Libro más descargado y menos descargado ~.~
                    
                    0 - Salir ~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~
                    
                    Selecciona la opción que deseas ejecutar:
                    """;
            System.out.println(menu);
            while (!teclado.hasNextInt()) {
                System.out.println("¡Ingrese un número que esté disponible en el menú!");
                teclado.nextLine();
            }
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                case 1 -> buscarLibroPorTitulo();
                case 2 -> listaLibrosRegistrados();
                case 3 -> listaAutoresRegistrados();
                case 4 -> autoresVivosDate();
                case 5 -> listaLibrosPorIdioma();
                case 6 -> top10LibrosMasDescargados();
                case 7 -> rankingLibro();
                case 0 -> System.out.println("Saliendo de la aplicación...");
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private DatosBusqueda getBusqueda () {
        System.out.println("Escribe el nombre del libro: ");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "%20"));
        return convierte.obtenerDatos(json, DatosBusqueda.class);
    }

    private void buscarLibroPorTitulo() {
        DatosBusqueda datosBusqueda = getBusqueda();
        if (datosBusqueda != null && !datosBusqueda.resultadoLibros().isEmpty()) {
            DatosLibros datosLibros = datosBusqueda.resultadoLibros().get(0);
            Libro libro = new Libro(datosLibros);

            Optional<Libro> libroExiste = libroRepository.findByTitulo(libro.getTitulo());
            if (libroExiste.isPresent()) {
                System.out.println("Libro ya registrado!");
            } else {
                if (!datosLibros.autor().isEmpty()) {
                    DatosAutor datosAutor = datosLibros.autor().get(0);

                    Autor autor = new Autor(datosAutor);
                    Optional<Autor> autorOptional = autorRepository.findByNombre(autor.getNombre());

                    if (autorOptional.isPresent()) {
                        Autor autorExiste = autorOptional.get();
                        Hibernate.initialize(autorExiste.getLibros());
                        libro.setAutor(autorExiste);
                    } else {
                        Autor autorNuevo = autorRepository.save(autor);
                        libro.setAutor(autorNuevo);
                    }
                    libroRepository.save(libro);
                }
                System.out.println(libro);
                System.out.println("~.~.~.~ ~.~.~.~ ~.~.~.~");
            }
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void listaLibrosRegistrados() {
        System.out.println("~.~.~.~.~ Libros Registrados ~.~.~.~.~ ");
        List<Libro> librosRegistrados = libroRepository.findAll();
        librosRegistrados.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(System.out::println);
    }

    private void listaAutoresRegistrados() {
        System.out.println("~.~.~.~.~  Autores Registrados *~.~.~.~.~ ");
        List<Autor> autoresRegistrados = autorRepository.findAll();
        autoresRegistrados.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(System.out::println);
    }

    private void autoresVivosDate() {
        System.out.println("Ingrese el año que desea consultar:");
        Integer consulta = Integer.valueOf(teclado.nextLine());
        List<Autor> autores = autorRepository.buscarAutoresPorAnio(consulta);
        if (autores.isEmpty()) {
            System.out.println("No hay registros...");
        } else {
            System.out.println("~.~.~.~.~  Autores vivos durante el año " + consulta + " ~.~.~.~.~ ");
            autores.stream()
                    .sorted(Comparator.comparing(Autor::getNombre))
                    .forEach(System.out::println);
        }
    }


    private void listaLibrosPorIdioma() {
        List<String> idiomas = List.of("en", "es");
        idiomas.forEach(idioma -> {
            long count = libroRepository.countByIdioma(idioma);
            System.out.printf("Cantidad de libros en %s: %d%n", idioma, count);
        });
    }


    private void top10LibrosMasDescargados() {
        List<Libro> top10Libros = libroRepository.findTop10ByOrderByNumeroDescargasDesc();
        System.out.println("~.~.~.~ Top 10 Libros Más Descargados ~.~.~.~");
        top10Libros.forEach(libro -> System.out.printf("Titulo: %s, Autor: %s, Numero de Descargas: %d%n",
                libro.getTitulo(),
                libro.getAutor() != null ? libro.getAutor().getNombre() : "Autor desconocido",
                libro.getNumeroDescargas()));
        System.out.println("~.~.~.~ ~.~.~.~ ~.~.~.~");
    }

    private void rankingLibro() {
        List<Libro> libros = libroRepository.findAll();
        IntSummaryStatistics est = libros.stream()
                .filter(l -> l.getNumeroDescargas() > 0)
                .collect(Collectors.summarizingInt(Libro::getNumeroDescargas));

        Libro libroMasDescargado = libros.stream()
                .filter(l -> l.getNumeroDescargas() == est.getMax())
                .findFirst()
                .orElse(null);

        Libro libroMenosDescargado = libros.stream()
                .filter(l -> l.getNumeroDescargas() == est.getMin())
                .findFirst()
                .orElse(null);

        System.out.println(".~.~ ~.~.~.~ Ranking de Libros ~.~.~.~ ~.~");
        if (libroMasDescargado != null) {
            System.out.printf("Libro más descargado: %s%nNúmero de descargas: %d%n",
                    libroMasDescargado.getTitulo(), est.getMax());
            System.out.println("~.~.~.~ ~.~.~.~ ~.~.~.~.~.~.~.~ ~.~.~.~ ~.~.~.~");
        }
        if (libroMenosDescargado != null) {
            System.out.println("~.~.~.~ ~.~.~.~ ~.~.~.~~.~.~.~ ~.~.~.~ ~.~.~.~");
            System.out.printf("Libro menos descargado: %s%nNúmero de descargas: %d%n",
                    libroMenosDescargado.getTitulo(), est.getMin());
        }
    }
}

