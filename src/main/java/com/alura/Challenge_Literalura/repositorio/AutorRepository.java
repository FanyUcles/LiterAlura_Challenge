package com.alura.Challenge_Literalura.repositorio;

import com.alura.Challenge_Literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query(value = "SELECT * FROM autor WHERE EXTRACT(YEAR FROM fecha_nacimiento) = :anio", nativeQuery = true)
    List<Autor> findAutoresVivosEnAnio(@Param("anio") int anio);

    Autor findByNombre(String nombre);
}



