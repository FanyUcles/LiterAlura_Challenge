package com.alura.Challenge_Literalura.repositorio;

import com.alura.Challenge_Literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    @Query("SELECT l FROM Libro l WHERE LOWER(l.titulo) LIKE LOWER(concat('%', :titulo, '%'))")
    List<Libro> buscarPorTitulo(String titulo);

    @Query("SELECT l FROM Libro l")
    List<Libro> listarLibros();

    @Query("SELECT l FROM Libro l WHERE LOWER(l.idioma) = LOWER(:idioma)")
    List<Libro> listarPorIdioma(String idioma);

    List<Libro> findByIdioma(String idioma);
}






