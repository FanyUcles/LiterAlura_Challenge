package com.alura.Challenge_Literalura.service;

import com.alura.Challenge_Literalura.model.Libro;
import com.alura.Challenge_Literalura.repositorio.AutorRepository;
import com.alura.Challenge_Literalura.repositorio.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    @Autowired
    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.buscarPorTitulo(titulo);
    }

    public List<Libro> listarLibros() {
        return libroRepository.listarLibros();
    }

    public List<String> listarAutores() {
        return AutorRepository.listarAutores();
    }

    public List<String> listarAutoresVivos(int anio) {
        return AutorRepository.listarAutoresVivos(anio);
    }

    public List<Libro> listarPorIdioma(String idioma) {
        return libroRepository.listarPorIdioma(idioma);
    }
}

