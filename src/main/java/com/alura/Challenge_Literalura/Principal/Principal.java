package com.alura.Challenge_Literalura.Principal;

import com.alura.Challenge_Literalura.model.Autor;
import com.alura.Challenge_Literalura.model.Datos;
import com.alura.Challenge_Literalura.model.Libro;
import com.alura.Challenge_Literalura.model.LibroResponse;
import com.alura.Challenge_Literalura.repositorio.AutorRepository;
import com.alura.Challenge_Literalura.repositorio.LibroRepository;
import com.alura.Challenge_Literalura.service.ConsumoAPI;
import com.alura.Challenge_Literalura.service.ConvierteDatos;
import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;
import java.util.List;


public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private ConsumoAPI consulta = new ConsumoAPI();
    private int opcion = -1;
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    List<Autor> autores;
    List<Libro> libros;
    Scanner teclado = new Scanner(System.in);

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void consultar(){

        do{
            mostrarMenu();
            try{
            opcion = Integer.valueOf(teclado.nextLine());
            switch(opcion) {
                case 1:
                    buscarLibroWeb();
                    break;
                case 2:
                    mostrarLibrosBuscados();
                    break;
                case 3:
                    mostrarAutoresBuscados();
                    break;
                case 4:
                    mostrarAutoresPorAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Finalizando el programa");
                    break;
                default:
                    System.out.println("Opcion no valida. Favor ingresar correctamente");
            }
            }catch(NumberFormatException e){
                System.out.println("Favor ingresar correctamente");
            }
        } while  (opcion != 0);


    }

    public void mostrarMenu(){
        System.out.println("""
                
                -------------------------------------------
                -----------------Bienvendo-----------------
                
                Por Favor seleccione una opción para continuar:
                1- buscar libro por titulo
                2- Listar libros registrados
                3- Listar autores registrados
                4- Listar autores vivos en un determinado año
                5- listar libros por idioma
                6- Estadisticas
                7- Top 10 libros mas descargados
                
                0- Salir
                -------------------------------------------
                -------------------------------------------
                
                """);
    }

    public void muestraMenuIdionas(){
        System.out.println("""
                
                -------------------------------------------
                Ingrese el idioma para buscar los libros:
                en- ingles
                es- español
                fr- frances
                pt- portugués
                -------------------------------------------
                """);
    }

    private void buscarLibroWeb(){
        System.out.println("Ingrese el nombre del libro que desea buscar: ");
        String libros = teclado.nextLine();

        String busqueda = "?search=" + libros.replace(" ","+");
        var json = consulta.obtenerDatos(URL_BASE + busqueda);
        var datos = conversor.obtenerDatos(json, Datos.class);

        LibroResponse datoslibro = datos.resultados().get(0);
        Libro libro = new Libro(datoslibro);
        Autor autor = new Autor().obtenerPrimerAutor(datoslibro);

        System.out.println(libro);
        guardarLibroConAutor(libro, autor);
    }

    private void guardarLibroConAutor(Libro libro, Autor autor){
        Optional<Autor> autorBuscado = autorRepository.findByNombreContains(autor.getNombre());

        if(autorBuscado.isPresent()){
            libro.setAutor(autorBuscado.get());
        } else {
            autorRepository.save(autor);
            libro.setAutor(autor);
        }

        try {
            libroRepository.save(libro);
        } catch (Exception e) {
            System.out.println("Ocurrió un error al guardar el libro: " + e.getMessage());
        }
    }



    private void mostrarLibrosBuscados() {
        libros = libroRepository.findAll();
        imprimeLibrosOrdenadosPorNombre(libros);
    }


    private void mostrarAutoresBuscados() {
        autores = autorRepository.findAll();
        imprimeAutoresOrdenadosPorNombre(autores);
    }

    private void mostrarAutoresPorAnio(){
        System.out.println("Ingrese el año que desea ver los autores");
        Integer anio = Integer.valueOf(teclado.nextLine());

        autores = autorRepository
                .findByFechaDeNacimientoLessThanEqualAndFechaDeMuerteGreaterThanEqual
                        (anio, anio);

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en ese año");
        } else {
            imprimeAutoresOrdenadosPorNombre(autores);
        }
    }

    private void imprimeAutoresOrdenadosPorNombre(List<Autor> autores){
        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(System.out::println);
    }

    private void imprimeLibrosOrdenadosPorNombre(List<Libro> libros) {
        libros.stream()
                .sorted(Comparator.comparing(Libro::getNombreAutor))
                .forEach(System.out::println);
    }

    private void listarLibrosPorIdioma(){
        muestraMenuIdionas();
        String idioma = teclado.nextLine();

        String claveIdioma;
        if (idioma.length() >= 2) {
            claveIdioma = idioma.substring(0, 2);
        } else {
            claveIdioma = idioma;
        }

        libros = libroRepository.findByIdiomasContains(claveIdioma);

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en ese idioma");
        } else {
            imprimeLibrosOrdenadosPorNombre(libros);
        }

    }

}

