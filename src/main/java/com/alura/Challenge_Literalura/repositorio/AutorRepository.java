package com.alura.Challenge_Literalura.repositorio;

import com.alura.Challenge_Literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByNombre(String nombre);

    List<Autor> findAutoresVivosEnAnio(int anio);

    @Query("SELECT DISTINCT l.autor FROM Libro l")
    static List<String> listarAutores() {
        return listarAutores();
    }

    @Query("SELECT DISTINCT l.autor FROM Libro l WHERE l.autor.fechaNacimiento < :anio")
    static List<String> listarAutoresVivos(int anio) {
        return listarAutoresVivos(anio);
    }
}


