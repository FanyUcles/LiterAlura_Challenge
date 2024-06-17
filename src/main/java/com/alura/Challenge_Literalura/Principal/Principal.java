package com.alura.Challenge_Literalura.Principal;

import com.alura.Challenge_Literalura.model.Autor;
import com.alura.Challenge_Literalura.model.Libro;
import com.alura.Challenge_Literalura.repositorio.AutorRepository;
import com.alura.Challenge_Literalura.repositorio.LibroRepository;
import com.alura.Challenge_Literalura.service.ConsumoAPI;
import com.alura.Challenge_Literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Component
public class Principal implements CommandLineRunner{

    private final Scanner scanner = new Scanner(System.in);
    private static final String URL_BASE = "https://gutendex.com/books/";
    private static final String URL_SEARCH_BY_NAME = "?search=";

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private ConsumoAPI consumoAPI;

    @Autowired
    private ConvierteDatos conversor;

    @Override
    public void run(String... args) {
        mostrarMenu();
    }

    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("""
                    Selecciona la opción ingresando el número correspondiente:
                    1 - Buscar libro por título.
                    2 - Listar libros registrados.
                    3 - Listar autores registrados.
                    4 - Listar autores vivos en un determinado año.
                    5 - Listar libros por idioma.
                    
                    0 - Salir.
                    """);

            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (opcion) {
                    case 1 -> buscarLibroPorTitulo();
                    case 2 -> listarLibrosRegistrados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> listarAutoresVivosEnAnio();
                    case 5 -> listarLibrosPorIdioma();
                    case 0 -> System.out.println("Saliendo...");
                    default -> System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                scanner.nextLine(); // Limpiar el buffer de entrada
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el título del libro que desea buscar:");
        String titulo = scanner.nextLine();
        String tituloCodificado = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
        String url = URL_BASE + URL_SEARCH_BY_NAME + tituloCodificado;
        String datos = consumoAPI.obtenerDatos(url);

        try {
            List<Libro> libros = conversor.obtenerDatos(datos, List.class);
            guardarLibros(libros);
            mostrarLibros(libros);
        } catch (Exception e) {
            System.out.println("Error al procesar los datos del API: " + e.getMessage());
        }
    }

    private void guardarLibros(List<Libro> libros) {
        libros.forEach(libro -> {
            if (libro.getAutor() != null) {
                Autor autorExistente = autorRepository.findByNombre(libro.getAutor().getNombre());
                if (autorExistente != null) {
                    libro.setAutor(autorExistente);
                } else {
                    autorRepository.save(libro.getAutor());
                }
            }
            libroRepository.save(libro);
        });
    }

    private void mostrarLibros(List<Libro> libros) {
        libros.forEach(libro -> System.out.println("Título: " + libro.getTitulo() + ", Autor: " + libro.getAutor().getNombre()));
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        libros.forEach(libro -> System.out.println(libro.getTitulo() + " - Autor: " + libro.getAutor().getNombre()));
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        autores.forEach(autor -> {
            System.out.println(autor.getNombre());
            autor.getLibros().forEach(libro -> System.out.println("  Libro: " + libro.getTitulo()));
        });
    }

    private void listarAutoresVivosEnAnio() {
        try {
            System.out.println("Ingrese el año:");
            int anio = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            List<Autor> autores = autorRepository.findAutoresVivosEnAnio(anio);
            autores.forEach(autor -> System.out.println(autor.getNombre()));
        } catch (InputMismatchException e) {
            System.out.println("Año inválido. Por favor, ingrese un número válido.");
            scanner.nextLine(); // Limpiar el buffer de entrada
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Ingrese el idioma (por ejemplo: 'en' para inglés): ");
        String idioma = scanner.nextLine();
        List<Libro> libros = libroRepository.findByIdioma(idioma);
        libros.forEach(libro -> System.out.println(libro.getTitulo() + " - Autor: " + libro.getAutor().getNombre()));
    }
}


