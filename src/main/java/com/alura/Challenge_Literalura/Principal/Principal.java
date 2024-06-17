package com.alura.Challenge_Literalura.Principal;

import com.alura.Challenge_Literalura.model.Autor;
import com.alura.Challenge_Literalura.model.Libro;
import com.alura.Challenge_Literalura.model.ResponseLibros;
import com.alura.Challenge_Literalura.repositorio.AutorRepository;
import com.alura.Challenge_Literalura.repositorio.LibroRepository;
import com.alura.Challenge_Literalura.service.ConsumoAPI;
import com.alura.Challenge_Literalura.service.ConvierteDatos;
import com.alura.Challenge_Literalura.service.IConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Principal implements CommandLineRunner {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static int option = -1;

    private final String URL_BASE = "https://gutendex.com/books/";
    private final String URL_SEARCH_BY_NAME = "?search=";

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    private final IConvierteDatos conversor = new ConvierteDatos();
    private final ConsumoAPI consumoAPI = new ConsumoAPI();

    public static void main(String[] args) {
        SpringApplication.run(Principal.class, args);
    }

    @Override
    public void run(String... args) {
        app();
    }

    public void app() {
        while (option != 0) {
            String MENU = """
                    Selecciona la opción ingresando el número correspondiente:
                    1 - Buscar libro por título.
                    2 - Listar libros registrados.
                    3 - Listar autores registrados.
                    4 - Listar autores vivos en un determinado año.
                    5 - Listar libros por idioma.
                    
                    0 - Salir.
                    """;
            System.out.println(MENU);
            option = SCANNER.nextInt();
            SCANNER.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
        SCANNER.close();
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el título del libro:");
        String titulo = SCANNER.nextLine();
        String tituloCodificado = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
        String url = URL_BASE + URL_SEARCH_BY_NAME + tituloCodificado;
        String datos = consumoAPI.obtenerDatos(url);

        // Parsear la respuesta JSON y guardar en la base de datos
        List<Libro> libros = conversor.obtenerDatos(datos, ResponseLibros.class).getResults();
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

        // Mostrar los datos de una forma más legible
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
        System.out.println("Ingrese el año:");
        int anio = SCANNER.nextInt();
        SCANNER.nextLine(); // Consume newline
        List<Autor> autores = autorRepository.findAutoresVivosEnAnio(anio);
        autores.forEach(autor -> System.out.println(autor.getNombre()));
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Ingrese el idioma (por ejemplo: 'en' para inglés):");
        String idioma = SCANNER.nextLine();
        List<Libro> libros = libroRepository.findByIdioma(idioma);
        libros.forEach(libro -> System.out.println(libro.getTitulo() + " - Autor: " + libro.getAutor().getNombre()));
    }
}
