package com.alura.Challenge_Literalura.repositorio;

import com.alura.Challenge_Literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByIdiomasContains(String idiomas);
}






